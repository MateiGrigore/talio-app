package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class AdminMenuViewCtrl {
    private final TalioMainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    @FXML
    private AnchorPane background;

    private List<Board> boards = new ArrayList<>();

    private List<Pane> boardPanes = new ArrayList<>();

    /**
     * @param mainCtrl    The main controller class
     * @param serverUtils The Server Utils class
     */
    @Inject
    public AdminMenuViewCtrl(TalioMainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    /**
     * Loads all the boards from the database and subscribes to the websocket
     */
    public void loadBoards() {
        boards = serverUtils.getAllBoards();

        serverUtils.registerForMessages("/topic/remove-boards", List.class, boardList -> {
            boards = boardList;
            Platform.runLater(this::redrawBoards);

        });

        serverUtils.registerForMessages("/topic/edit-board", Board.class, newBoard -> {
            for (Board board : boards) {
                if (board.id == newBoard.id) {
                    board.title = newBoard.title;
                }
            }

            Platform.runLater(this::redrawBoards);
        });

        serverUtils.registerForMessages("/topic/remove-board", Long.class, boardId -> {
            boards.removeIf(board -> board.id == boardId);
            Platform.runLater(this::redrawBoards);
        });

        serverUtils.registerForMessages("topic/new-board", Board.class, board -> {
            boards.add(board);
            Platform.runLater(this::redrawBoards);
        });
        serverUtils.registerForMessages("/topic/join-board", User.class, user -> {
            Platform.runLater(this::redrawBoards);
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
    public void createBoard() {
        mainCtrl.showCreateBoardView(true);
    }

    /**
     * Refreshes the content of the page to be up-to-date with the database
     */
    public void redrawBoards() {
        boards = serverUtils.getAllBoards();
        clearBoardsFromScreen();

        int boardCounter = 0;

        for (Board board : boards) {
            Pane newBoard = new Pane();
            newBoard.setLayoutX(60 + (boardCounter % 3) * 150);
            newBoard.setLayoutY(30 + 100 * (boardCounter / 3 + 1));
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
                    mainCtrl.showBoardView(true);
                }

                if (event.getButton() == MouseButton.SECONDARY) {
                    mainCtrl.showEditBoardView(board, true);
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

    /**
     * Deletes all boards form server
     */
    public void deleteBoardsFromServer() {
        serverUtils.deleteAllBoards(boards);
        mainCtrl.initializeAdminMenuView();
        mainCtrl.showAdminMenuView();
    }
}
