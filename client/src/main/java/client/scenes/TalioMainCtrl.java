package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.concurrent.TimeUnit;

/**
 * Main Controller class for the board app.
 * Acts as a middle ground to avoid cyclic dependency.
 * Manages all the other Controllers.
 */
public class TalioMainCtrl {

    private Stage primaryStage;

    private Stage cardOverview;
    private BoardViewCtrl boardViewCtrl;
    private ConnectToServerViewCtrl connectToServerViewCtrl;
    private BoardMenuViewCtrl boardMenuViewCtrl;
    private CreateBoardViewCtrl createBoardViewCtrl;

    private Scene boardView;
    private Scene connectToServerView;

    private Scene boardMenuView;
    private Scene createBoardView;


    private EditCardViewCtrl editCardViewCtrl;
    private Scene editCardView;

    private EditBoardViewCtrl editBoardViewCtrl;

    private Scene editBoardView;

    private AdminMenuViewCtrl adminMenuViewCtrl;

    private Scene adminMenuView;

    private ServerUtils serverUtils;

    private Scene boardTagOverviewView;

    private BoardTagOverviewCtrl boardTagOverviewCtrl;

    private Scene addTagView;

    private AddTagViewCtrl addTagViewCtrl;


    /**
     * Initializes the primary stage and displays the first scene in the app
     *
     * @param primaryStage        The primary stage of the app
     * @param boardView           A Controller-Parent pair for the Board View scene
     * @param connectToServerView A Controller-Parent pair for the Connect To Server scene
     * @param boardMenuView       A Controller-Parent pair for the Board Menu scene
     * @param createBoardView     A Controller-Parent pair for the Create Board scene
     * @param editCardView        A Controller-Parent pair for the Edit Card scene
     * @param editBoardView       A Controller-Parent pair for the Edit Board scene
     * @param adminMenuView       A Controller-Parent pair for the Admin Menu scene
     * @param serverUtils         The Server Utils class
     * @param boardTagOverview    A Controller-Parent pair for the Board Tag Overview
     * @param addTagView          A Controller-Parent pair for the Add Tag scene
     */
    public void initialize(Stage primaryStage, Pair<BoardViewCtrl, Parent> boardView,
                           Pair<ConnectToServerViewCtrl, Parent> connectToServerView,
                           Pair<BoardMenuViewCtrl, Parent> boardMenuView,
                           Pair<CreateBoardViewCtrl, Parent> createBoardView,
                           Pair<EditCardViewCtrl, Parent> editCardView,
                           Pair<EditBoardViewCtrl, Parent> editBoardView,
                           Pair<AdminMenuViewCtrl, Parent> adminMenuView,
                           Pair<BoardTagOverviewCtrl, Parent> boardTagOverview,
                           Pair<AddTagViewCtrl, Parent> addTagView,
                           ServerUtils serverUtils) {
        this.primaryStage = primaryStage;
        this.cardOverview = new Stage();

        this.boardViewCtrl = boardView.getKey();
        this.boardView = new Scene(boardView.getValue());

        this.connectToServerViewCtrl = connectToServerView.getKey();
        this.connectToServerView = new Scene(connectToServerView.getValue());

        this.boardMenuViewCtrl = boardMenuView.getKey();
        this.boardMenuView = new Scene(boardMenuView.getValue());

        this.createBoardViewCtrl = createBoardView.getKey();
        this.createBoardView = new Scene(createBoardView.getValue());

        this.editCardViewCtrl = editCardView.getKey();
        this.editCardView = new Scene(editCardView.getValue());

        this.editBoardViewCtrl = editBoardView.getKey();
        this.editBoardView = new Scene(editBoardView.getValue());

        this.adminMenuViewCtrl = adminMenuView.getKey();
        this.adminMenuView = new Scene(adminMenuView.getValue());

        this.boardTagOverviewCtrl = boardTagOverview.getKey();
        this.boardTagOverviewView = new Scene(boardTagOverview.getValue());

        this.addTagViewCtrl = addTagView.getKey();
        this.addTagView = new Scene(addTagView.getValue());


        this.serverUtils = serverUtils;

        showConnectToServerView();
        primaryStage.show();
    }

    /**
     * Changes the primary stage to the Board View scene
     *
     * @param isAdmin boolean to indicate if the user who called the method is an admin
     */
    public void showBoardView(Boolean isAdmin) {
        boardViewCtrl.setAdmin(isAdmin);
        primaryStage.setTitle("Boards: Board View");
        primaryStage.setScene(boardView);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        boardViewCtrl.redrawBoard();
    }

    /**
     * @param board Board to be displayed in the Board View
     */
    public void initializeBoardView(Board board) {
        boardViewCtrl.setBoardAndInitialize(board);
    }

    /**
     * Changes the primary stage to the Connect To Server scene
     */
    public void showConnectToServerView() {
        connectToServerViewCtrl.setServerPassword();
        primaryStage.setTitle("Connect to Server");
        primaryStage.setScene(connectToServerView);
    }


    /**
     * Changes the primary stage to the Board Menu scene
     */
    public void showBoardMenuView() {

        primaryStage.setTitle("Board Menu");
        primaryStage.setScene(boardMenuView);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        loadUpdatedBoards();
        boardMenuViewCtrl.redrawBoards();
    }

    /**
     * Changes the primary stage to the Admin Menu scene
     */
    public void showAdminMenuView() {
        primaryStage.setTitle("Admin Menu");
        primaryStage.setScene(adminMenuView);

        initializeAdminMenuView();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        adminMenuViewCtrl.redrawBoards();
    }

    /**
     * Initializes the Board Menu View
     */
    public void loadUpdatedBoards() {
        boardMenuViewCtrl.loadUpdatedBoards(serverUtils.getJoinedBoards());
    }

    //TODO try to remove after

    /**
     * Initializes the Admin Menu View
     */
    public void initializeAdminMenuView() {
        adminMenuViewCtrl.loadBoards();
    }

    /**
     * Changes the primary stage to the Create Board scene
     *
     * @param isAdmin boolean to indicate if the user who called the method is an admin
     */
    public void showCreateBoardView(boolean isAdmin) {

        createBoardViewCtrl.setAdmin(isAdmin);

        primaryStage.setTitle("Create Board");
        primaryStage.setScene(createBoardView);

    }

    /**
     * Changes the primary stage to the Edit Board scene
     *
     * @param board   the board that sets the state of the scene
     * @param isAdmin boolean to indicate if the user who called the method is an admin
     */
    public void showEditBoardView(Board board, boolean isAdmin) {
        primaryStage.setTitle("Edit board");
        primaryStage.setScene(editBoardView);
        editBoardViewCtrl.setBoard(board);
        editBoardViewCtrl.setAdmin(isAdmin);
    }


    /**
     * Closes the EdictCard scene
     */
    public void closeCardOverview() {
        Platform.runLater(() -> {
            cardOverview.close();
        });
    }

    /**
     * Changes the card overview stage to the EdictCard scene
     *
     * @param card the card that is being edited
     */
    public void showCardOverview(Card card) {
        editCardViewCtrl.setState(card);
        editCardViewCtrl.setupCard();
        cardOverview.setScene(editCardView);
        cardOverview.show();
    }


    /**
     * Getter for the server port
     */
    public void showAddTagView() {
        primaryStage.setTitle("Add Tag");
        primaryStage.setScene(addTagView);
        addTagViewCtrl.setBoard(boardViewCtrl.getBoardShown());
    }

    /**
     * Changes the primary stage to the Board Tag Overview scene
     */
    public void showBoardTagOverview() {
        primaryStage.setTitle("Board Tags");
        primaryStage.setScene(boardTagOverviewView);
        boardTagOverviewCtrl.setBoard(boardViewCtrl.getBoardShown());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        boardTagOverviewCtrl.drawTags();
    }
}
