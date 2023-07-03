package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateBoardViewCtrl {
    private final TalioMainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    private TextField title;

    private boolean isAdmin;

    private BoardMenuViewCtrl boardMenuViewCtrl;

    private AdminMenuViewCtrl adminMenuViewCtrl;

    /**
     * @param mainCtrl    The main controller class
     * @param serverUtils The Server Utils class
     * @param boardMenuViewCtrl The Board Menu View Controller class
     * @param adminMenuViewCtrl The Admin Menu View Controller class
     */
    @Inject
    public CreateBoardViewCtrl(TalioMainCtrl mainCtrl, ServerUtils serverUtils,
                               BoardMenuViewCtrl boardMenuViewCtrl,
                               AdminMenuViewCtrl adminMenuViewCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = serverUtils;
        this.boardMenuViewCtrl = boardMenuViewCtrl;
        this.adminMenuViewCtrl = adminMenuViewCtrl;
    }


    /**
     * Sends POST request to server to add a new board to the database
     * @return The new board
     */
    @FXML
    private void createBoard() throws InterruptedException {
        Board board = new Board();

        board.title = title.getText();
        title.clear();

        if(isAdmin){
            server.sendNewBoardToServer(board);
            mainCtrl.showAdminMenuView();
            adminMenuViewCtrl.redrawBoards();
        } else{
            server.sendToWebsocket("/app/add-board/" + board.title, server.getUser());
            mainCtrl.showBoardMenuView();
            boardMenuViewCtrl.redrawBoards();
        }

    }

    /**
     * Cancels the process and returns to the main view
     */
    public void cancel() throws InterruptedException {
        mainCtrl.showBoardMenuView();
    }

    /**
     * Setter for the isAdmin field
     * Shows if the person who entered the scene is an admin or not
     * @param isAdmin the value to set to
     */
    public void setAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }
}
