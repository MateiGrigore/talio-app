package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.ListEntity;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class EditListViewCtrl {


    private final TalioMainCtrl mainCtrl;

    @FXML
    private TextField name;

    private long id;
    private final ServerUtils serverUtils;
    private Board board;

    private Boolean isAdmin;

    /**
     * Setter for the board
     *
     * @param board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * @param mainCtrl    The main controller class
     * @param serverUtils The Server Utils class
     */
    @Inject
    public EditListViewCtrl(TalioMainCtrl mainCtrl, ServerUtils serverUtils) {

        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    /**
     * Sends POST request to server to delete the list in the database
     */
    @FXML
    public void deleteList() throws InterruptedException {
        ListEntity list = new ListEntity();
        list.id = id;
        list.board = board;
        serverUtils.deleteList(list);

        mainCtrl.showBoardView(isAdmin);
    }

    /**
     * Sends POST request to server to update the list in the database
     */
    @FXML
    public void updateList() throws InterruptedException {
        ListEntity list = new ListEntity();
        list.id = id;
        list.name = name.getText();
        list.board = board;

        serverUtils.updateList(list);

        mainCtrl.showBoardView(isAdmin);
    }

    /**
     * Sets the state of the scene with the attributes of the selected list
     *
     * @param list the list to be edited
     */
    public void setState(ListEntity list) {
        name.setText(list.name);
        id = list.id;
    }

    /**
     * Cancels the process and returns to the main view
     */
    public void cancel() throws InterruptedException {
        mainCtrl.showBoardView(isAdmin);
    }

    /**
     * Setter for the isAdmin field
     * Shows if the person who entered the scene is an admin or not
     * @param isAdmin the value to set to
     */
    public void setAdmin(Boolean isAdmin){
        this.isAdmin = isAdmin;
    }
}
