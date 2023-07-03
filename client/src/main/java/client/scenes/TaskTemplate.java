package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class TaskTemplate {

    public enum Direction { UP, DOWN }

    @FXML
    public CheckBox isCompleted;

    @FXML
    public AnchorPane pane;
    private ServerUtils serverUtils;

    @FXML
    public ImageView upButton;

    @FXML
    public ImageView downButton;

    @FXML
    public ImageView cross;

    @FXML
    public javafx.scene.control.TextField title;

    private Task task;

    /**
     * Helper method to set up the task controller
     * @param utils ServerUtils instance
     * @param task Task for which the set-up is being done
     */
    public void loadUtils(ServerUtils utils, Task task) {
        this.serverUtils = utils;
        this.task = task;
    }

    /**
     * Helper method that calls the server to rename the task
     */
    public void rename() {
        this.task.text = title.getText();
        serverUtils.editTask(task);
    }

    /**
     * Inverts the completion status
     */
    public void invertStatus() {
        this.task.completed = !this.task.completed;
        serverUtils.editTask(task);
    }

    /**
     * Moves a task in a given direction (UP/DOWN)
     * @param direction Direction in which the task is moved
     */
    public void moveDirection(Direction direction) {
        if(direction.equals(Direction.UP)) {
            // lower rank = higher in list!
            if(task.rank <= 0) return; // do nothing if at top of task list
            serverUtils.updateTaskRanks(this.task, this.task.rank-1);
        } else {
            serverUtils.updateTaskRanks(this.task, this.task.rank+1);
        }
    }
}
