package client.utils;

import client.Main;
import client.scenes.BoardViewCtrl;
import client.scenes.CardCtrl;
import client.scenes.TalioMainCtrl;
import commons.Card;
import commons.ListEntity;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;


public class DragDropUtils {

    /**
     * These four variables allow modifying the layout a bit
     * so that spacing and card size can be adjusted
     */
    private final int initialOffset;
    // this ^ offset controls the spacing away from the title (top of list)

    private final int cardHeight;
    // this ^ controls the height of the card as it appears on the screen

    private final int spacing;
    // this ^ controls the spacing between the cards

    private double offsetFromTopWindow;
    // this ^ controls offset from window, allowing a check
    // that the edit menu is not being clicked

    private ServerUtils server;

    private BoardViewCtrl control;

    private TalioMainCtrl mainCtrl;

    private boolean isDragged;

    /**
     * @return initial offset from top of list
     */
    public int getInitialOffset() {
        return initialOffset;
    }

    /**
     * Sets the offset from top of window relative to the list position
     *
     * @param offsetFromTopWindow y-position of list background
     */
    public void setOffsetFromTopWindow(double offsetFromTopWindow) {
        this.offsetFromTopWindow = offsetFromTopWindow;
    }

    /**
     * Constructor that allows creating the drag & drop functionality
     *
     * @param initialOffset offset from top of list
     * @param cardHeight    height of card
     * @param spacing       spacing between cards
     * @param serverUtils   client-side server utility specific to
     *                      the server to which the client is connected
     * @param boardViewCtrl controller for the board view
     * @param mainCtrl the main controller of the app
     */
    public DragDropUtils(
            int initialOffset,
            int cardHeight,
            int spacing,
            ServerUtils serverUtils,
            BoardViewCtrl boardViewCtrl,
            TalioMainCtrl mainCtrl
    ) {
        this.initialOffset = initialOffset;
        this.cardHeight = cardHeight;
        this.spacing = spacing;
        this.server = serverUtils;
        this.control = boardViewCtrl;
        this.mainCtrl = mainCtrl;
    }

    /**
     * @return offset from the top of window
     */
    public double getOffsetFromTopWindow() {
        return offsetFromTopWindow;
    }

    /**
     * Adds a basic style to a created card
     *
     * @param newCard created card
     * @param rank    card's rank in list
     * @param card   the card object
     */
    public void styleCard(Pane newCard, int rank, Card card) {
        newCard.setLayoutY(calculatePosY(rank));

        newCard.setPrefHeight(this.cardHeight);
        newCard.setPrefWidth(150);
        newCard.setLayoutX(15);
        newCard.setStyle("-fx-background-color: rgb(145,144,144); -fx-background-radius: 7");

        addDragDropHandler(newCard, card);
    }

    /**
     * A method that adds a drag-and-drop handler to a card
     * @param cardEntity the card Entity
     * @param card card to which the handler is being added
     */
    public void addDragDropHandler(Pane card, Card cardEntity) {
        // add a cursor indicator

        card.setOnMouseEntered(mouseEvent -> {
            card.setCursor(Cursor.CLOSED_HAND);
        });

        var pair = Main.FXML.load(CardCtrl.class, "client", "scenes", "CardTemplate.fxml");
        Pane cardMock = (Pane) pair.getValue();
        final CardCtrl ctrl = pair.getKey();
        ctrl.setState(cardEntity);
        setupMockCard(card, cardMock);

        //set the event handler for the actions on the card pane
        card.addEventHandler(MouseEvent.ANY,
                new MouseHandler(
                        mouseEvent -> {
                            setDragHandler(card, mouseEvent, cardMock);
                        },
                        event -> {
                            mainCtrl.showCardOverview(cardEntity);
                        }
                )
        );
        // put in correct rank
        addReleaseHandler(card);
    }

    /** Sets the dragging handler to the card pane
     * @param card the card that the event is being set on
     * @param mouseEvent the event that triggered the setting
     * @param cardMock the mock pane of the card
     */
    public void setDragHandler(Pane card, MouseEvent mouseEvent, Pane cardMock){
        if (mouseEvent.getSceneY() > this.offsetFromTopWindow
                + 2 * this.initialOffset) {
            card.setLayoutY(mouseEvent.getSceneY()
                    - (this.initialOffset + this.cardHeight));

            long displacement = (Long.parseLong(card.getParent().getId()) - 1)
                    * 240;

            card.setLayoutX(mouseEvent.getSceneX() - (50) - displacement);

            moveCardMock(cardMock, mouseEvent);
        }
    }

    /**
     * Adds the handler for MouseReleased
     * @param card card that was moved
     */
    private void addReleaseHandler(Pane card) {
        card.setOnMouseReleased(mouseEvent -> {

            long cardID = Long.parseLong(card.getId());

            // update the list first
            long listNum = calculateHorizontalRank(card); // want to get nth list in board
            long oldListID = server.getCardByID(cardID).list.id;

            ListEntity nthPosition = server.getNthListInBoard(control.board.id, listNum);
            long listID = nthPosition == null ? oldListID : nthPosition.id;

            server.updateCardsList(cardID, listID);

            // send a request to server to update ranks
            server.updateRanks(cardID, calculateRank(card));

            server.squashRanks(oldListID);
        });
    }

    /**
     * Sets up the mock card whose overlay is added during drag
     * @param card card on which this mock is based on
     * @param cardMock instantiated card mock
     */
    private void setupMockCard(Pane card, Pane cardMock) {
        cardMock.setStyle(card.getStyle());
        cardMock.setPrefWidth(card.getPrefWidth());
        cardMock.setPrefHeight(card.getPrefHeight());
        cardMock.setLayoutY(card.getLayoutY());
        cardMock.setLayoutX(card.getLayoutX());
    }

    /**
     * Sets the position of the Mock Card so that it acts
     * as an overlay during drag
     * @param cardMock instantiated mock card used as overlay
     * @param mouseEvent mouse event that contains cursor position
     */
    private void moveCardMock(Pane cardMock, MouseEvent mouseEvent) {
        // drag the mock
        cardMock.setLayoutX(mouseEvent.getSceneX() - 29);
        cardMock.setLayoutY(mouseEvent.getSceneY() -
                (this.initialOffset + this.cardHeight) + 20);
        cardMock.toFront();
        if (!control.listBackground.getChildren().contains(cardMock)) {
            control.listBackground.getChildren().add(cardMock);
        } else {
            int id = control.listBackground.getChildren().indexOf(cardMock);
            control.listBackground.getChildren().set(id, cardMock);
        }
    }

    /**
     * Formula for the card rank:
     * <p>
     * posY = rank * (cardHeight + spacing) + initialOffset <-->
     * rank = (posY - initialOffset) / (cardHeight + spacing)
     * <p>
     * // remark:  rank is also the num of cards
     * before the specified card
     * <p>
     * since each new card is `rank` * `spacing` away from top,
     * and additional `rank` * `cardHeight` away (since rank can be interpreted
     * as the number of cards before a given card, each of them having additional spacing
     * that follows it), and additionally `initialOffset` from the top
     *
     * @param card a card for which rank is being calculated
     * @return calculated rank
     **/
    public int calculateRank(Pane card) {
        return ((int) card.getLayoutY() - this.initialOffset) /
                (this.cardHeight + this.spacing);
    }

    /**
     * Calculates the horizontal rank, which is used to determine
     * to which list the card was dragged to
     * @param card Card being dragged
     * @return computed horizontal rank
     */
    public int calculateHorizontalRank(Pane card) {
        int displacement = (Integer.parseInt(card.getParent().getId()) - 1) * 240;
        return (((int) card.getLayoutX() + 20) + displacement)/240;
    }

    /**
     * See javadoc in `calculateRank` to see computations for rank and posY
     * Calculates the position of a card based on its rank
     *
     * @param rank integer deciding the position of the card
     * @return Y position in list
     */
    public double calculatePosY(int rank) {
        return rank * (this.cardHeight + this.spacing) + this.initialOffset;
    }
}
