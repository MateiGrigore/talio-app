package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class BoardTagOverviewCtrl {
    private final TalioMainCtrl mainCtrl;
    private final ServerUtils server;

    private List<Tag> tags = new ArrayList<>();

    private Board board;

    @FXML
    public AnchorPane tagBackground;

    /**
     * @param mainCtrl The main controller class
     * @param server  The Server Utils class
     */
    @Inject
    public BoardTagOverviewCtrl(TalioMainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * @param board The board to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * @param board The board to load the tags from
     */
    void loadTagDataFromServer(Board board) {
        tags = server.getTags(board);
    }

    /**
     * Draws the tags
     */
    public void drawTags() {
        loadTagDataFromServer(board);

        removeTags();

        int maxSizePerRow = 4;

        int nhorizontal = 1;
        int nvertical = 1;

        for (Tag tag : tags) {
            Pane tagPane = new Pane();
            tagPane.setPrefSize(200, 200);
            tagPane.setLayoutX(nhorizontal * 250);
            tagPane.setLayoutY(nvertical * 150);


            Text tagTitle = new Text(tag.getName());
            tagTitle.setFill(javafx.scene.paint.Color.rgb(tag.r, tag.g, tag.b));

            tagPane.getChildren().add(tagTitle);

            tagPane.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    server.sendToWebsocket("/app/remove-tag/" + board.id, tag);
                    System.out.println("Removed tag " + tag.getName());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    drawTags();
                }
            });

            tagBackground.getChildren().add(tagPane);

            nhorizontal++;

            if (nhorizontal > maxSizePerRow) {
                nhorizontal = 1;
                nvertical++;
            }
        }
    }

    /**
     * Removes all tags
     */
    public void removeTags() {
        tagBackground.getChildren().clear();
    }

    /**
     * Shows the add tag view
     */
    @FXML
    public void addTag() {
        mainCtrl.showAddTagView();
    }

    /**
     * Shows the board overview
     */
    @FXML
    public void backToBoardOverview() {
        mainCtrl.showBoardView(false);
    }
}
