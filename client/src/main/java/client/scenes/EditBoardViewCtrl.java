package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditBoardViewCtrl {

    private final TalioMainCtrl mainCtrl;

    private final ServerUtils serverUtils;

    private Board board;

    private boolean isAdmin;

    @FXML
    private TextField name;

    @FXML
    private Label boardLabel;

    /**
     * Constructor for the Edit Board View Controller
     * @param mainCtrl
     * @param serverUtils
     */
    @Inject
    public EditBoardViewCtrl(TalioMainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    /**
     * Sets the board field
     * which is used to set the text field to the name of the current board
     * @param board the board to set the state to
     */
    public void setBoard(Board board){
        this.board = board;
        boardLabel.setText(board.title);
        name.setText(board.title);
    }

    /**
     * Cancels the action and returns to Board Menu
     * @throws InterruptedException
     */
    public void cancel() throws InterruptedException {
        if(isAdmin){
            mainCtrl.showAdminMenuView();
        } else{
            mainCtrl.showBoardMenuView();
        }
    }

    /**
     * Sens request to update the board with the new attributes
     * @throws InterruptedException
     */
    public void updateBoard(){
        Board newBoard = new Board();
        newBoard.id = board.id;
        newBoard.title = name.getText();

        serverUtils.updateBoard(newBoard);

        if(isAdmin){
            mainCtrl.initializeAdminMenuView();
            mainCtrl.showAdminMenuView();
        } else{
            mainCtrl.loadUpdatedBoards();
            mainCtrl.showBoardMenuView();
        }
    }

    /**
     * Sends request to delete the board
     * @throws InterruptedException
     */
    public void deleteBoard() {
        serverUtils.deleteBoard(board);

        if(isAdmin){
            mainCtrl.initializeAdminMenuView();
            mainCtrl.showAdminMenuView();
        } else{
            mainCtrl.loadUpdatedBoards();
            mainCtrl.showBoardMenuView();
        }
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
