package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.ListEntity;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ListCtrl {

    private final TalioMainCtrl mainCtrl;
    private final ServerUtils serverUtils;

    private BoardViewCtrl boardViewCtrl;

    private ListEntity list;

    @FXML
    private TextField title;

    /** Constructor for the CardCTrl
     * @param mainCtrl the main controller
     * @param serverUtils the server utils
     */
    @Inject
    public ListCtrl(TalioMainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    /** Sets the state of the list
     * @param list the list to be set
     */
    public void setState(ListEntity list) {
        title.setText(list.name);
        this.list = list;
    }

    /**
     * Calls the method to rename the list
     */
    public void rename(){
        this.list.name = title.getText();
        serverUtils.updateList(list);
    }

    /** Setter for the boardViewCtrl
     * @param boardViewCtrl the boardViewCtrl
     */
    public void setBoardViewCtrl(BoardViewCtrl boardViewCtrl) {
        this.boardViewCtrl = boardViewCtrl;
    }

    /**
     *Method for the remove list button
     */
    public void removeList(){
        serverUtils.deleteList(list);
    }
}
