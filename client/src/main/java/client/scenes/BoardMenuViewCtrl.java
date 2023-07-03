package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class BoardMenuViewCtrl {
    private final TalioMainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    @FXML
    private AnchorPane background;

    @FXML
    private TextField id;

    @FXML
    private Label errorMessage;
    private List<Board> joinedBoards = new ArrayList<>();

    private List<Pane> boardPanes = new ArrayList<>();

    /**
     * @param mainCtrl    The main controller class
     * @param serverUtils The Server Utils class
     */
    @Inject
    public BoardMenuViewCtrl(TalioMainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    /**
     * Loads all the boards from the database and subscribes to the websocket
     *
     * @param updatedBoards The new list of boards
     */
    public void loadUpdatedBoards(List<Board> updatedBoards) {
        this.joinedBoards = updatedBoards;

        serverUtils.registerForMessages("/topic/edit-board", Board.class, newBoard -> {
            for (Board board : joinedBoards) {
                if (board.id == newBoard.id) {
                    board.title = newBoard.title;
                }
            }
            Platform.runLater(() -> redrawBoards());
        });

        serverUtils.registerForMessages("/topic/remove-board", Long.class, boardId -> {
            joinedBoards.removeIf(board -> board.id == boardId);
            Platform.runLater(() -> redrawBoards());
        });
        serverUtils.registerForMessages("/topic/remove-boards", List.class, boards -> {
            joinedBoards = new ArrayList<>();
            Platform.runLater(() -> redrawBoards());
        });

        serverUtils.registerForMessages("/topic/join-board", User.class, user -> {
            joinedBoards = serverUtils.getJoinedBoards();
            Platform.runLater(() -> redrawBoards());
        });

    }

    /**
     * Go back to the "connect to server view scene"
     */
    public void showConnectToServerView() {
        mainCtrl.showConnectToServerView();
    }

    /**
     * Go to the "Create Board" view scene
     */
    @FXML
    private void createBoard() {
        mainCtrl.showCreateBoardView(false);
    }

    /**
     * Checks if the board ID in the id TextField is valid,
     * and adds the board with this id to the joinedBoards list
     */
    @FXML
    public void joinBoard() {
        String boardId = id.getText();

        errorMessage.setVisible(true);
        if (boardId.isEmpty()) {
            errorMessage.setText("Enter a board ID.");
        } else if (!validID(boardId)) {
            errorMessage.setText("This board does not exist.");
        } else if (isBoardJoined(boardId)) {
            errorMessage.setText("You have already joined this board");
        } else {
            for (Board board : serverUtils.getAllBoards()) {
                if (Long.toString(board.id).equals(boardId)) {
                    serverUtils.sendToWebsocket("/app/join-board/" +
                            board.id, serverUtils.getUser());
                    break;
                }

            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            loadUpdatedBoards(serverUtils.getJoinedBoards());
            redrawBoards();
        }
        id.clear();
    }

    /**
     * Checks whether the user has joined a board
     *
     * @param boardId The ID of the board
     * @return true if the user has already joined this board, false otherwise
     */
    private boolean isBoardJoined(String boardId) {
        for (Board board : joinedBoards) {
            if (Long.toString(board.id).equals(boardId))
                return true;
        }
        return false;
    }

    /**
     * Checks is a board exists in the database
     *
     * @param boardId The ID of the board
     * @return true if the board exists, false otherwise
     */
    private boolean validID(String boardId) {
        for (Board board : serverUtils.getAllBoards()) {
            if (Long.toString(board.id).equals(boardId))
                return true;
        }
        return false;
    }

    /**
     * Refreshes the content of the page to be up-to-date with the database
     */
    public void redrawBoards() {
        clearBoardsFromScreen();
        if(errorMessage != null)
            errorMessage.setVisible(false);

        int boardCounter = 0;

        for (Board board : joinedBoards) {
            Pane newBoard = new Pane();
            newBoard.setLayoutX(115 + (boardCounter % 7) * 150);
            newBoard.setLayoutY(30 + 100 * (boardCounter / 7 + 1));
            newBoard.setPrefHeight(70);
            newBoard.setPrefWidth(130);
            newBoard.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 7");

            Label title = new Label();
            title.setText(board.title);
            newBoard.getChildren().add(title);
            title.setPrefHeight(70);
            title.setPrefWidth(130);
            title.setStyle("-fx-font-size: 18px");
            title.setStyle("-fx-alignment: center");

            newBoard.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    mainCtrl.initializeBoardView(board);
                    mainCtrl.showBoardView(false);
                }

                if(event.getButton() == MouseButton.SECONDARY){
                    mainCtrl.showEditBoardView(board, false);
                }

            });


            background.getChildren().add(newBoard);
            boardPanes.add(newBoard);

            boardCounter++;

        }
    }

    /**
     * Clears all the boards from the screen
     */
    public void clearBoardsFromScreen() {
        for (Pane board : boardPanes) {
            background.getChildren().remove(board);
        }
        boardPanes.clear();
    }
}
