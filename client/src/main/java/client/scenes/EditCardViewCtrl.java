package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class EditCardViewCtrl {
    private final TalioMainCtrl mainCtrl;
    @FXML
    private Text completed;

    @FXML
    private TextField name;

    @FXML
    private Text counter;

    @FXML
    private TextArea description;

    @FXML
    private TextField text;

    @FXML
    private ScrollPane taskPane;

    @FXML
    private Pane taskList;

    private Card selectedCard;

    private long id;
    private final ServerUtils serverUtils;
    private Board board;
    private List<Task> tasks;

    private final int spacingFromTop = 10;
    private final int spacingBetween = 10;
    private final int taskHeight = 30;
    private final int taskWidth = 155;

    private final int unitsMoved = (spacingBetween + taskHeight);


    /**
     * @param mainCtrl    The main controller class
     * @param serverUtils The Server Utils class
     */
    @Inject
    public EditCardViewCtrl(TalioMainCtrl mainCtrl, ServerUtils serverUtils) {

        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;


    }

    /**
     * Method for initializing the card websocket listeners
     */
    public void setupCard() {
        this.taskPane.setBackground(
                new Background(
                    new BackgroundFill(Color.rgb(254,254,254), new CornerRadii(7), null))
        );
        this.taskPane.setBorder(
                new Border(
                        new BorderStroke(
                                Color.BLACK, BorderStrokeStyle.DASHED,
                                new CornerRadii(7),
                                new BorderWidths(0.9),
                                new Insets(-0.4))
                )
        );
        loadTasks();
        serverUtils.registerForMessages(
                "/topic/new-task/" + this.selectedCard.id, Task.class, task -> {
                    Platform.runLater(this::loadTasks);
                });
        serverUtils.registerForMessages(
                "/topic/remove-task/" + this.selectedCard.id, Task.class, task -> {
                    Platform.runLater(this::loadTasks);
                });
        serverUtils.registerForMessages(
                "/topic/modify-task/" + this.selectedCard.id, Task.class, task -> {
                    Platform.runLater(this::loadTasks);
                });
        serverUtils.registerForMessages(
                "/topic/squash-tasks/" + this.selectedCard.id, Card.class, card -> {
                    Platform.runLater(this::loadTasks);
                }
        );
        serverUtils.registerForMessages(
                "/topic/move-task/" + this.selectedCard.id, Card.class, card -> {
                    Platform.runLater(this::loadTasks);
                }
        );
    }

    private void renderTask(Task taskEntity, Pane task, long position, String text) {
        double posX = 10;
        double posY = position * this.unitsMoved + this.spacingFromTop;
        task.setLayoutX(posX);
        task.setLayoutY(posY);

        var pair = Main.FXML.load(TaskTemplate.class, "client", "scenes", "TaskTemplate.fxml");
        Pane taskPane = (Pane) pair.getValue();
        task.getChildren().add(taskPane);
        final TaskTemplate ctrl = pair.getKey();

        // set title of task
        ctrl.title.setText(text);
        ctrl.title.setBorder(
                new Border(
                        new BorderStroke(
                                Color.rgb(12, 12, 12, 0.5),
                                BorderStrokeStyle.DASHED,
                                new CornerRadii(5),
                                new BorderWidths(0.5),
                                new Insets(-0.4)
                        )
                )
        );
        ctrl.loadUtils(serverUtils, taskEntity);

        // set status (checkbox)
        ctrl.isCompleted.setSelected(taskEntity.completed);

        // get clickable delete cross
        Node clickableCross = ctrl.cross;
        clickableCross.setOnMouseClicked((mouseEvent) -> {
            this.serverUtils.deleteTask(taskEntity);
            this.serverUtils.squashTaskRanks(this.selectedCard.id);
        });
        clickableCross.setOnMouseEntered((mouseEvent) -> {
            clickableCross.setCursor(Cursor.HAND);
        });
        clickableCross.setOnMouseExited((mouseEvent) -> {
            clickableCross.setCursor(Cursor.DEFAULT);
        });

        setupMoveButtons(ctrl);
    }


    private void setupMoveButtons(TaskTemplate ctrl) {
        // setup move-up button
        Node clickableUp = ctrl.upButton;
        clickableUp.setOnMouseClicked((mouseEvent) -> {
            ctrl.moveDirection(TaskTemplate.Direction.UP);
        });
        clickableUp.setOnMouseEntered((mouseEvent) -> {
            clickableUp.setCursor(Cursor.HAND);
        });
        clickableUp.setOnMouseExited((mouseEvent) -> {
            clickableUp.setCursor(Cursor.DEFAULT);
        });

        // setup move-down button
        Node clickableDown = ctrl.downButton;
        clickableDown.setOnMouseClicked((mouseEvent) -> {
            ctrl.moveDirection(TaskTemplate.Direction.DOWN);
        });
        clickableDown.setOnMouseEntered((mouseEvent) -> {
            clickableDown.setCursor(Cursor.HAND);
        });
        clickableDown.setOnMouseExited((mouseEvent) -> {
            clickableDown.setCursor(Cursor.DEFAULT);
        });
    }

    private void updateCounter() {
        int count = this.serverUtils.getNumberOfTasksOnCard(this.selectedCard.id);
        this.counter.setText(Integer.toString(count));
        int complete = this.serverUtils.getNumberOfTasksCompletedOnCard(this.selectedCard.id);
        this.completed.setText(Integer.toString(complete));
    }

    private void loadTasks() {

        // clear all current tasks in list
        this.tasks = new ArrayList<>();

        // remove all children
        while(!this.taskList.getChildren().isEmpty()) {
            this.taskList.getChildren().remove(0);
        }

        // set task list to fetched tasks
        this.tasks = serverUtils.getTasksOnCard(this.selectedCard.id);

        // add render of task per each card
        for(int i=0; i<this.tasks.size(); i++) {
            String text = tasks.get(i).text;

            Pane pane = new Pane();
            renderTask(tasks.get(i), pane, tasks.get(i).rank, text);
            taskList.getChildren().add(pane);

            if(this.tasks.size() < 5) this.taskList.setPrefHeight(246);
            else this.taskList.setPrefHeight(
                    this.tasks.size() * this.unitsMoved + this.spacingFromTop
            );
        }

        updateCounter();
    }

    /**
     * Sends POST request to server to delete the card in the database
     */
    @FXML
    public void deleteCard(){
        serverUtils.deleteCard(selectedCard);
        mainCtrl.closeCardOverview();
    }

    /**
     * Sends POST request to server to update the card in the database
     */
    @FXML
    public void updateCard() {
        selectedCard.name = name.getText();
        selectedCard.description = description.getText();

        serverUtils.updateCard(selectedCard, selectedCard.list.board.id);

        mainCtrl.closeCardOverview();
    }

    /**
     * Sets the state of the scene with the attributes of the selected card
     * @param card the card to be edited
     */
    public void setState(Card card) {
        name.setText(card.name);
        description.setText(card.description);
        this.selectedCard = card;
        id = card.id;
        //register for card removal updates
        serverUtils.registerForUpdates(cardUpdate -> {
            //if the current card was removed close the overview
            if(cardUpdate.equals(card)){
                cancel();
            }
        });
    }


    /**
     * Cancels the process and returns to the main view
     */
    public void cancel(){
        mainCtrl.closeCardOverview();
    }

    /**
     * Sends a request to the server to add a new task to DB
     */
    public void addTask() {
        Task mockTask = new Task();
        mockTask.text = this.text.getText();
        mockTask.card = this.selectedCard;
        this.serverUtils.sendNewTaskToServer(mockTask, this.id);
    }

    /**
     * Setter for the board
     *
     * @param board the board to be edited
     */
    public void setBoard(Board board) {
        this.board = board;
    }


    /**
     * Stop receiving updates from the server
     */
    public void stop(){
        serverUtils.stop();
    }
}
