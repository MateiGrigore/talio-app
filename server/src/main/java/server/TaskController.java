package server;

import commons.Card;
import commons.Task;
import server.database.CardRepository;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class TaskController {
    private TaskRepository tasks;
    private CardRepository cards;
    private CardController cardController;

    /**
     * @param tasks
     * @param cards
     * @param cardController
     */
    public TaskController(TaskRepository tasks, CardRepository cards,
                          CardController cardController) {
        this.tasks = tasks;
        this.cards = cards;
        this.cardController = cardController;
    }

    /**
     * @return All the tasks in the database
     */
    public List<Task> getAllTasks() {
        return tasks.findAll();
    }

    /**
     * @param id The id of the task
     * @return The task with the given id
     */
    public Task getTasksByID(long id) {
        return tasks.findById(id).orElse(null);
    }

    /**
     * @param cardId The id of the card
     * @return All the tasks in the given card
     */
    public List<Task> getTasksByCardId(long cardId) {

        Optional<Card> optionalList = cards.findById(cardId);
        if (optionalList.isEmpty()) {
            return new ArrayList<>();
        }

        return tasks.findAllTasksByCardId(cardId);
    }
    /**
     * @param task   The task to be added
     * @param cardId The id of the card to which the task will be added
     * @return True if the task was added successfully, false otherwise
     */
    public boolean addTask(Task task, long cardId) {
        Card cardInDB = cardController.getCardByID(cardId);

        if (cardInDB == null) return false;
        incrementTaskRanks(cardId, 0L);

        task.setCard(cardInDB);
        tasks.save(task);

        return true;
    }

    /**
     * @param task The task to be updated
     * @return The updated task
     */
    public Task saveTaskOverwrite(Task task) {
        return tasks.save(task);
    }

    /**
     * Deletes a task from DB
     *
     * @param task Task to be deleted
     * @return True if the task was deleted successfully, false otherwise
     */
    public boolean deleteTask(Task task) {

        Optional<Task> optionalTask = tasks.findById(task.id);
        if (optionalTask.isEmpty()) return false;

        tasks.deleteById(task.id);

        return true;
    }

    /**
     * Modifies a task's text in DB
     *
     * @param task Task to have its description modified
     * @return True if task edited, else false
     */
    public boolean editTask(Task task) {

        Optional<Task> optionalTask = tasks.findById(task.id);
        if (optionalTask.isEmpty()) return false;

        Task found = optionalTask.get();

        found.text = task.text;
        found.completed = task.completed;

        tasks.save(found);

        return true;
    }

    /**
     * Fetches the number of tasks on a card from DB
     *
     * @param card card on which the tasks are
     * @return Integer : number of tasks
     */
    public int getNumberOfTasks(Card card) {
        return tasks.getNumAll(card.id);
    }

    /**
     * Increments task ranks. Used during task creation
     * to correctly order new tasks
     * @param cardID ID of card on which the tasks are
     * @param rank Minimal rank that will be affected
     * @return true iff successful, else false
     */
    public boolean incrementTaskRanks(long cardID, long rank) {
        tasks.incrementAllTaskRanksGTERank(cardID, rank);
        return true;
    }

    /**
     * @param task    The task to be moved
     * @param newRank The new rank of the task
     * @return task affected
     */
    public Task moveTaskAndRestructure(Task task, long newRank) {

        // get task that was moved
        Task taskFromRepo = getTasksByID(task.id);
        if (taskFromRepo == null) return null;

        long cardID = taskFromRepo.card.id;

        // fetch all other tasks
        List<Task> other = tasks.getOtherTasks(cardID, taskFromRepo.id);

        // convert to an iterator
        Iterator<Task> iterator = other.iterator();

        int maxSpaces = other.size() + 1;

        Task[] arrTasks = new Task[maxSpaces];

        // iterate over positions & add to list
        for (int pos = 0; pos < maxSpaces; pos++) {
            if (pos != newRank && iterator.hasNext()) {
                arrTasks[pos] = iterator.next();
            } else {
                arrTasks[pos] = taskFromRepo;
            }
        }

        // move each task
        for (int i = 0; i < maxSpaces; i++) {
            arrTasks[i].rank = (long) i;
            tasks.save(arrTasks[i]);
        }

        return task;
    }

    /**
     * Removes empty space between tasks when their rank is affected.
     * This helps during task deletion (when an empty spot would be left otherwise)
     * @param cardID ID of card on which a task is being moved
     * @return Card on which the tasks were squashed
     */
    public Card squashTaskRanks(long cardID) {

        // fetch all tasks
        List<Task> fetchedTasks = tasks.getOtherTasks(cardID, -1);
        int maxSpaces = fetchedTasks.size();

        // convert to an iterator
        Iterator<Task> iterator = fetchedTasks.iterator();

        Task[] arrTasks = new Task[maxSpaces];

        // iterate over positions & add to list of tasks
        for (int pos = 0; pos < maxSpaces; pos++) {
            arrTasks[pos] = iterator.next();
        }

        // move each task
        for (int i = 0; i < maxSpaces; i++) {
            arrTasks[i].rank = (long) i;
            tasks.save(arrTasks[i]);
        }

        if (arrTasks.length > 0) return arrTasks[0].card;
        else return null;
    }

    /**
     * Fetches the number of tasks with complete=true
     * from the DB
     *
     * @param card card on which the tasks are
     * @return Integer : number of _complete_ tasks
     */
    public int getNumberOfCompletedTasks(Card card) {
        return tasks.getNumCompleted(card.id);
    }
}
