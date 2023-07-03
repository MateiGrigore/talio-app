package server.database;

import commons.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    /** Return all tasks belonging to a certain card
     * @param cardId id of the card
     * @return all the tasks belonging
     */
    @Query("SELECT t FROM Task t WHERE t.card.id = ?1")
    List<Task> findAllTasksByCardId(long cardId);

    /**
     * Fetches the number of completed tasks on a certain card
     * @param cardId card on which the tasks reside
     * @return Integer number of tasks completed
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.completed = true AND t.card.id = ?1")
    Integer getNumCompleted(long cardId);

    /**
     * Fetches the number of all tasks on a given card
     * @param cardId card on which the tasks reside
     * @return Integer number of tasks completed
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.card.id = ?1")
    Integer getNumAll(long cardId);

    /**
     * Increments ranks of all tasks on a given card, GTE given rank
     * @param cardID ID of card on which tasks reside
     * @param rank minimal rank being updated
     * @return number of affected tuples
     */
    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.rank = t.rank+1 WHERE t.card.id = ?1 AND t.rank >= ?2")
    int incrementAllTaskRanksGTERank(long cardID, long rank);

    /**
     * A query that gets all tasks except a task with selected ID
     * @param taskID ID of card on which the cards are
     * @param excludedTaskID ID of card that must be excluded
     * @return list of tasks that match the query
     */
    @Query("SELECT t FROM Task t WHERE t.card.id = ?1 AND t.id != ?2 ORDER BY t.rank ASC")
    List<Task> getOtherTasks(long taskID, long excludedTaskID);
}
