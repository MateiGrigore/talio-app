import commons.Board;
import commons.Card;
import commons.ListEntity;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AppController;
import server.RandomPassword;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskControllerTest {
    private DummyBoardRepository dummyBoardRepository;
    private DummyListRepository dummyListRepository;
    private DummyCardRepository dummyCardRepository;
    private DummyTaskRepository taskRepository;
    private AppController controller;

    private RandomPassword randomPassword;

    /**
     * Sets up a clean testing environment with dummy repositories
     * before each test
     */
    @BeforeEach
    private void setupTestingEnv() throws FileNotFoundException {
        // setup dummy repositories
        this.dummyBoardRepository = new DummyBoardRepository();
        this.dummyListRepository = new DummyListRepository();
        this.dummyCardRepository = new DummyCardRepository();
        this.taskRepository = new DummyTaskRepository();

        // setup app controller
        this.controller = new AppController(
                this.dummyBoardRepository,
                this.dummyListRepository,
                this.dummyCardRepository,
                this.taskRepository,
                null,
                null,
                randomPassword
        );

        setupHelper();
    }

    private void setupHelper() throws FileNotFoundException {
        // create two boards for testing
        addSetupBoard(1, "first board");
        addSetupBoard(2, "Team Board Number 2");

        // create two lists in board 1
        addSetupList(1, "first list", 1L);
        addSetupList(2, "second list", 1L);

        // create one card in each list
        addSetupCard(1, "first card", 0L, 1L);
        addSetupCard(2, "second card", 1L, 2L);
    }

    /**
     * Helper for creating a new default Board for testing
     * @throws FileNotFoundException when files not found
     */
    private void addSetupBoard(
            long id,
            String title

    ) {
        Board board = new Board();
        board.id = id;
        board.title = title;

        this.controller.addBoard(board);
    }

    private void addSetupList(
            long id,
            String name,
            long boardID
    ) {
        ListEntity list = new ListEntity();
        list.id = id;
        list.name = name;
        list.board = this.dummyBoardRepository.getById(boardID);

        this.controller.addList(list, Long.toString(boardID));
    }

    private void addSetupCard(
            long id,
            String name,
            long rank,
            long listID
    ) {
        Card card = new Card();
        card.id = id;
        card.name = name;
        card.rank = rank;
        card.list = this.dummyListRepository.getById(listID);

        this.controller.addCard(card);
    }

    private void addSetupTask(
            long id,
            String text,
            long cardID,
            boolean complete
    ) {
        Task task = new Task();
        task.id = id;
        task.text = text;
        task.card = this.dummyCardRepository.getById(cardID);
        task.completed = complete;

        this.controller.addTask(task);
    }

    /**
     * Tests fetching all tasks from the repository;
     * Here, testing if correctly returns empty list if none present
     */
    @DisplayName("Fetches all tasks correctly when empty")
    @Test
    public void correctlyFetchesTasksEmpty() {
        assertEquals(new ArrayList<>(), this.controller.getAllTasks());
    }

    /**
     * Tests `addTask`
     * @throws FileNotFoundException
     */
    @DisplayName("Correctly creates task 1 on card 1")
    @Test
    public void correctlyCreatesTask1() throws FileNotFoundException {
        addSetupTask(1L, "first task, with ID=1", 1L, false);

        Task task = new Task();
        task.id = 1L;
        task.text = "first task, with ID=1";
        task.card = this.controller.getCardByID(1L);

        assertEquals(task, this.taskRepository.tasks.get(0));
    }

    /**
     * Tests `addTask`
     * @throws FileNotFoundException
     */
    @DisplayName("Correctly creates task 2 on card 2")
    @Test
    public void correctlyCreatesTask2() throws FileNotFoundException {
        addSetupTask(2L, "second task, with ID=2", 2L, false);

        Task task = new Task();
        task.id = 2L;
        task.text = "second task, with ID=2";
        task.card = this.controller.getCardByID(2L);

        assertEquals(task, this.taskRepository.tasks.get(0));
    }

    /**
     * Tests `getAllTasks`
     * @throws FileNotFoundException if files missing
     */
    @DisplayName("Fetches all tasks correctly when two added")
    @Test
    public void correctlyFetchesTasksPopulated() throws FileNotFoundException {
        addSetupTask(1L, "first task, with ID=1", 1L, false);
        addSetupTask(2L, "second task, with ID=2", 2L, false);

        Task task1 = new Task();
        task1.id = 1L;
        task1.text = "first task, with ID=1";
        task1.card = this.controller.getCardByID(1L);

        Task task2 = new Task();
        task2.id = 2L;
        task2.text = "second task, with ID=2";
        task2.card = this.controller.getCardByID(2L);

        List<Task> taskList = new ArrayList<>();
        taskList.add(task1);
        taskList.add(task2);

        assertEquals(taskList, this.controller.getAllTasks());
    }

    /**
     * Tests `getTaskByID`
     * @throws FileNotFoundException
     */
    @DisplayName("Fetches task by ID")
    @Test
    public void correctlyFetchesTaskByID() throws FileNotFoundException {
        addSetupTask(1L, "first task, with ID=1", 1L, false);
        addSetupTask(2L, "second task, with ID=2", 2L, false);

        Task task2 = new Task();
        task2.id = 2L;
        task2.text = "second task, with ID=2";
        task2.card = this.controller.getCardByID(2L);

        assertEquals(task2, this.controller.getTasksByID(2L));
    }

    /**
     * Tests `modifyTask()`
     */
    @DisplayName("Edits task by ID")
    @Test
    public void correctlyEditsTask() {
        addSetupTask(1L, "first task, with ID=1", 1L, false);

        Task original = this.controller.getTasksByID(1);
        Task created = new Task();
        created.completed = !original.completed;
        created.id = original.id;
        created.card = original.card;

        // get the update task
        this.controller.modifyTask(created);
        Task taskExtracted = this.controller.getTasksByID(1);

        assertEquals(taskExtracted, created);
    }

    /**
     * Tests `removeTask()`
     */
    @DisplayName("Delete Task")
    @Test
    public void correctlyDeletesTask() {

        addSetupTask(1L, "first task, with ID=1", 1L, false);

        Task taskExtracted = this.controller.getTasksByID(1);
        assertNotNull(taskExtracted);

        // delete the task
        this.controller.removeTask(taskExtracted);
        taskExtracted = this.controller.getTasksByID(1);

        // assert
        assertNull(taskExtracted);
    }

    @DisplayName("Get number of tasks on a card")
    @Test
    public void correctlyGetsNumberAllTasks() {

        int beforeAddition = this.controller.getNumberOfAllTasks(1);

        // add tasks to card & see if query returns correct result

        addSetupTask(1L, "first task, with ID=1", 1L, false);
        addSetupTask(2L, "second task, with ID=2", 1L, false); // same card ID here

        int afterAddition = this.controller.getNumberOfAllTasks(1);

        assertEquals(afterAddition - 2, beforeAddition);
        assertEquals(afterAddition, 2);
    }

    @DisplayName("Get number of _complete_ tasks on a card")
    @Test
    public void correctlyGetsNumberCompletedTasks() {

        int beforeAddition = this.controller.getNumberOfAllTasks(1);

        // add tasks to card & see if query returns correct result

        addSetupTask(1L, "first task, with ID=1", 1L, false);

        addSetupTask(2L, "second task, with ID=2", 1L, true); // completed

        int afterAddition = this.controller.getNumberOfCompletedTasks(1);

        assertEquals(afterAddition - 1, beforeAddition);
        assertEquals(afterAddition, 1);
    }

    @DisplayName("Get Tasks by cardID")
    @Test
    public void correctlyListsFindTaskByCardID() throws FileNotFoundException {
        addSetupTask(1L, "first task, with ID=1", 1L, false);
        addSetupTask(2L, "second task, with ID=2", 2L, false);

        // create model objects
       Task model1 = new Task();
        model1.id = 1;
        model1.text = "first task, with ID=1";
        model1.card = this.controller.getCardByID(1L);

       Task model2 = new Task();
        model2.id=2;
        model2.text = "second task, with ID=2";
        model2.card = this.controller.getCardByID(2L);

        // get all lists
        List<Task> tasksExtracted = this.controller.getTasksByCardId(1L);

        // assert
        assertEquals(List.of(model1), tasksExtracted);
    }

    @DisplayName("Updates the rank for one task on creation")
    @Test
    public void correctlyIncrementsRankOnAdd() {
        addSetupTask(1L, "first task, with ID=1", 1L, false);

        Task taskOne = this.controller.getTasksByID(1L);
        long rankTask1Before = taskOne.rank;

        addSetupTask(2L, "second task, with ID=2", 1L, false);

        taskOne = this.controller.getTasksByID(1L);
        long rankTask1After = taskOne.rank;

        // task One, which was added first, should have its rank incremented upon creation

        assertEquals(rankTask1Before + 1, rankTask1After);

    }

    /**
     * This method tests a databaseController method which is used in a key
     * method from AppController
     */
    @DisplayName("Fetches correct different tasks")
    @Test
    public void correctlyFetchesDifferentTasks() {
        addSetupTask(1L, "first task, with ID=1", 1L, false);
        addSetupTask(2L, "second task, with ID=2", 1L, false);
        addSetupTask(3L, "third task", 1L, true);

        List<Task> differentTasks = this.taskRepository.getOtherTasks(1L, 2L);

        List<Task> modelList = new ArrayList<>();
        modelList.add(this.controller.getTasksByID(3L));
        modelList.add(this.controller.getTasksByID(1L));

        assertEquals(modelList, differentTasks);
    }

    @DisplayName("Correctly moves tasks")
    @Test
    public void correctlyMovesTasks() {
        addSetupTask(1L, "first task, with ID=1", 1L, false);   // rank=2
        addSetupTask(2L, "second task, with ID=2", 1L, false);  // rank=1
        addSetupTask(3L, "third task", 1L, true);               // rank=0

        Task moved = this.controller.getTasksByID(2L);

        this.controller.moveTask(moved, 0L);
        // now should be:
        //      task ID         rank
        //      1               2
        //      2               0
        //      3               1

        // check if tasks were moved
        assertEquals(this.controller.getTasksByID(1).rank, 2L);
        assertEquals(this.controller.getTasksByID(2).rank, 0L);
        assertEquals(this.controller.getTasksByID(3).rank, 1L);
    }

    @DisplayName("Correctly squashes tasks after deletion")
    @Test
    public void correctlySquashesTasks() {
        addSetupTask(1L, "first task, with ID=1", 1L, false);   // rank=2
        addSetupTask(2L, "second task, with ID=2", 1L, false);  // rank=1
        addSetupTask(3L, "third task", 1L, true);               // rank=0

        // remove task with ID=2 and squash
        this.controller.removeTask(this.controller.getTasksByID(2L));
        this.controller.squashTasksInCard(1L);

        // now should be:
        //      task ID         rank
        //      1               1
        //      3               0

        assertEquals(this.controller.getTasksByID(1).rank, 1L);
        assertEquals(this.controller.getTasksByID(3).rank, 0L);
    }

}
