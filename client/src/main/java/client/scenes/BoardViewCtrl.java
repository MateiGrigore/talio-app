package client.scenes;

import client.Main;
import client.utils.DragDropUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.ListEntity;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoardViewCtrl {
    private final TalioMainCtrl mainCtrl;
    private final ServerUtils server;
    private final DragDropUtils dragDropUtils;

    @FXML
    private Label title;
    @FXML
    private TextField editBoard;
    @FXML
    private Label id;
    @FXML
    public AnchorPane listBackground;
    @FXML
    private Button addListBtn;

    public Board board;

    private List<ListEntity> lists;

    private HashMap<Long, List<Card>> allCards = new HashMap<>();

    private boolean isAdmin;


    /**
     * @param mainCtrl The main controller class
     * @param server   The Server Utils class
     */
    @Inject
    public BoardViewCtrl(TalioMainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.dragDropUtils = new DragDropUtils(70, 80, 10, server, this, mainCtrl);
    }

    /**
     * Loads the board data from the server and initializes the websocket subscriptions
     *
     * @param board Board to set
     */
    public void setBoardAndInitialize(Board board) {
        loadBoardDataFromServer(board);
        id.setText("ID: " + board.id);
        setRenameBoardEvents();
        redrawBoard();
        server.registerForMessages("/topic/new-list/" + board.id, ListEntity.class, list -> {
            lists.add(list);
            Platform.runLater(this::redrawBoard);
        });
        server.registerForMessages("/topic/new-card/" + board.id, Card.class, card -> {
            allCards.get(card.list.id).add(card);
            Platform.runLater(this::redrawBoard);
        });
        server.registerForMessages("/topic/edit-list/" + board.id, ListEntity.class, list -> {
            for (ListEntity l : lists) {
                if (l.id == list.id) {
                    l.name = list.name;
                }
            }
            Platform.runLater(this::redrawBoard);
        });

        server.registerForMessages("/topic/remove-list/" + board.id, Long.class, listID -> {
            lists.removeIf(l -> l.id == listID);
            Platform.runLater(this::redrawBoard);
        });

        server.registerForMessages("/topic/edit-card/" + board.id, Card.class, card -> {
            for (Card c : allCards.get(card.list.id)) {
                if (c.id == card.id) {
                    c.name = card.name;
                    c.description = card.description;
                }
            }
            Platform.runLater(this::redrawBoard);
        });

        server.registerForMessages("/topic/remove-card/" + board.id, Long.class, cardID -> {
            System.out.println("removing card with id: " + cardID);
            for (List<Card> cards : allCards.values()) {
                cards.removeIf(c -> c.id == cardID);
            }
            redrawBoard();
        });
        registerForDragDrop(board);
    }

    /**
     * Registers client for drag&drop-related events
     *
     * @param board board for which the event happens
     */
    public void registerForDragDrop(Board board) {
        server.registerForMessages("/topic/move-card/" + board.id, Card.class, card -> {
            allCards.put(card.list.id, server.getAllCardsFromList(card.list.id));
            redrawBoard();
        });
        server.registerForMessages("/topic/adjust-card/" + board.id, ListEntity.class, list -> {
            for (ListEntity l : lists) {
                if (l.id == list.id) {
                    l.name = list.name;
                }
            }
            redrawBoard();
        });
        server.registerForMessages("/topic/squash-cards/" + board.id, ListEntity.class, list -> {
            redrawBoard();
        });
    }

    /**
     * @return The board shown in the view
     */
    public Board getBoardShown() {
        return board;
    }

    /**
     * Switches to the Add List View
     */
    public void addBasicList() {
        ListEntity listEntity = new ListEntity();
        listEntity.board = board;
        listEntity.name = "Title";
        server.sendNewListToServer(listEntity, board.id);
    }


    /**
     * Refreshes the content of the page to be up-to-date with the database
     * Displays each list on the client
     *
     * @param board Board to refresh
     */
    public void loadBoardDataFromServer(Board board) {
        this.board = board;
        lists = server.getListsFromBoard(board);
        allCards = server.getCardHashMapFromBoard(board);
        title.setText(board.title);
    }

    /**
     * Redraws the board
     */
    public void redrawBoard() {
        Platform.runLater(() -> {
            clearBoardView();
            drawBoard();
        });

    }

    /**
     * Draws the entities on the board
     */
    public void drawBoard() {
        loadBoardDataFromServer(board);
//        System.out.println("Lists: " + lists.toString());
//        System.out.println("Cards: " + allCards.toString());

        int listCounter = 0;

        if (lists.isEmpty()) addListBtn.setLayoutX(611);

        for (ListEntity listEntity : lists) {
            int cardCounter = 0;
            var pair = Main.FXML.load(ListCtrl.class, "client", "scenes", "ListTemplate.fxml");
            Pane newList = (Pane) pair.getValue();
            final ListCtrl ctrl = pair.getKey();
            ctrl.setBoardViewCtrl(this);
            ctrl.setState(listEntity);
            Button addCard = new Button();
            addListVisualization(newList, addCard, listEntity, listCounter);
            List<Card> cardsThisList = this.allCards.get(listEntity.id);
            if (cardsThisList == null) continue;

            for (Card card : cardsThisList) {
                addCardVisualization(newList, addCard, card, (int) card.rank);
                cardCounter++;
            }
            listCounter++;
        }
    }

    /**
     * Clear all Lists and Buttons except the Add List button
     */
    public void clearBoardView() {
        List<Node> children = new ArrayList<>(listBackground.getChildren());
        for (Node child : children) {
            if (child != addListBtn) {
                listBackground.getChildren().remove(child);
            }
        }
    }

    /**
     * Helper method for displaying a List Entity
     *
     * @param newList     the pane of the List Entity
     * @param addCard     the Add Card button
     * @param list        the List Entity to be displayed
     * @param listCounter the number of the list. Used for positioning on screen
     */
    public void addListVisualization(Pane newList, Button addCard,
                                     ListEntity list, int listCounter) {
        //Added Scroll Pane for each list, so we can add multiple cards
        ScrollPane scrollPane = new ScrollPane();
        styleScrollPane(scrollPane, listCounter);

        newList.setLayoutX((listCounter) * 200);

        newList.setId(Long.toString(list.id));

        styleList(newList);

        scrollPane.setContent(newList);


        styleAddCardButton(addCard);
        addCardButtonEvent(addCard, list);

        newList.getChildren().add(addCard);

        addListBtn.setLayoutX(scrollPane.getLayoutX() + 200);
    }

    /**
     * Helper method for displaying cards
     *
     * @param listPane    the list pane in which the card is added
     * @param addCard     the Add Card button
     * @param card        the card that is added
     * @param cardCounter the number of the card. Used for positioning on screen
     */
    public void addCardVisualization(Pane listPane, Button addCard, Card card, int cardCounter) {

        //Pane newCard = new Pane();
        var pair = Main.FXML.load(CardCtrl.class, "client", "scenes", "CardTemplate.fxml");
        Pane newCard = (Pane) pair.getValue();
        final CardCtrl ctrl = pair.getKey();
        ctrl.setBoardViewCtrl(this);
        ctrl.setState(card);
        this.dragDropUtils.styleCard(newCard, cardCounter, card);


        // set ID so that the pane object can be directly linked to the card object
        newCard.setId(Long.toString(card.id));

        listPane.getChildren().add(newCard);
    }

    /**
     * Helper method for styling the pane of the ListEntity
     *
     * @param newList the pane of the ListEntity
     */
    public void styleList(Pane newList) {
        newList.setMinHeight(560);
        newList.setPrefWidth(180);
        newList.setStyle("-fx-background-color: rgb(255, 255 , 255)");
    }

    /**
     * Helper method for styling the scroll pane of the List Entity
     *
     * @param scrollPane  the scroll pane corresponding to the List Entity
     * @param listCounter the number of the list. Used for positioning on screen
     */
    public void styleScrollPane(ScrollPane scrollPane, int listCounter) {
        scrollPane.setLayoutX(20 + listCounter * 240);
        scrollPane.setLayoutY(18);
        scrollPane.setPrefHeight(560);
        scrollPane.setPrefWidth(190);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        listBackground.getChildren().add(scrollPane);
    }


    /**
     * Helper method for styling the Add Card button
     *
     * @param button the Add Card button
     */
    public void styleAddCardButton(Button button) {
        button.setText("Add Card");
        button.setLayoutY(42);
        button.setLayoutX(60);
        button.setStyle("-fx-background-radius: 7");
    }

    /**
     * Leaves the board and goes back to the Board Menu
     */
    @FXML
    void leaveBoard() {
        server.sendToWebsocket("/app/leave-board/" + board.id, server.getUser());
        showBoardMenuView();
    }

    /**
     * Go back to the Board Menu
     */
    public void showBoardMenuView(){
        if(isAdmin){
            mainCtrl.showAdminMenuView();
        } else {
            mainCtrl.showBoardMenuView();
        }
    }

    /**
     * Adds event listener to the button for adding a card
     *
     * @param button     the Add Card button
     * @param listEntity the List Entity where the Card will be added
     */
    public void addCardButtonEvent(Button button, ListEntity listEntity) {
        button.setOnAction(event -> {
            Card card = new Card();
            card.name = "Title";
            card.setList(listEntity);
            server.sendNewCardToServer(card, board.id);
            redrawBoard();
        });
    }

    /**
     * Configures events to update board name
     */
    public void setRenameBoardEvents() {
        title.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                title.setVisible(false);
                editBoard.setVisible(true);
                editBoard.setText(title.getText());
                editBoard.requestFocus();
            }
        });

        editBoard.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String newBoardName = editBoard.getText();
                title.setText(newBoardName);
                board.title = newBoardName;
                title.setVisible(true);
                editBoard.setVisible(false);
                server.updateBoard(board);
            }
        });
    }

    /**
     * Setter for the isAdmin field
     * Shows if the person who entered the scene is an admin or not
     * @param isAdmin the value to set to
     */
    public void setAdmin(Boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    /**
     * @return the state of isAdmin
     */
    public boolean getAdmin(){
        return isAdmin;
    }

    /**
     * Shows the board tag overview
     */
    public void showBoardTagOverview() {
        mainCtrl.showBoardTagOverview();
    }
}
