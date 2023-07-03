package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddTagViewCtrl {

    @FXML
    public TextField tagTitle;
    private Board board;

    public ServerUtils serverUtils;

    public TalioMainCtrl mainCtrl;

    /**
     * @param talioMainCtrl The main controller class
     * @param serverUtils  The Server Utils class
     */
    @Inject
    public AddTagViewCtrl(TalioMainCtrl talioMainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = talioMainCtrl;
        this.serverUtils = serverUtils;
    }

    /**
     * @param boardShown The board to add the tag to
     */
    public void setBoard(Board boardShown) {
        this.board = boardShown;
    }

    /**
     * Adds a tag to the board
     */
    @FXML
    public void addTag() {
        String title = tagTitle.getText();
        // generate rand int between 0 and 255 for r, g, b
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);

        Tag tag = new Tag(title, r, g, b);
        serverUtils.sendToWebsocket("/app/add-tag/" + board.id, tag);
        close();
    }

    /**
     * Closes the add tag view
     */
    @FXML
    public void close() {
        mainCtrl.showBoardTagOverview();
    }
}
