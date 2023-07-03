import commons.Task;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TaskRepository;

import java.util.*;
import java.util.function.Function;

public class DummyTaskRepository implements TaskRepository {
    public List<Task> tasks = new ArrayList<>();

    /**
     * Imitates the `findAll` implemented by the Jpa Repository
     * @return all tasks in repository
     */
    @Override
    public List<Task> findAll() {
        return this.tasks;
    }

    /**
     * Imitates the `save` implemented by the Jpa Repository
     * @param entity must not be {@literal null}.
     * @return the entity which was saved
     * @param <S> entity type being saved
     */
    @Override
    public <S extends Task> S save(S entity) {

        boolean updated = false;
        for(Task task : this.tasks) {
            if(task.id == entity.id) {
                int position = this.tasks.indexOf(task);
                this.tasks.set(position, entity);
                updated = true;
            }
        }

        if(!updated) {
            this.tasks.add(entity);
        }

        return entity;
    }

    /**
     * Imitates the `findByID` implemeted by the Jpa Repository
     * @param aLong must not be {@literal null}.
     * @return Optional of Task, if found
     */
    @Override
    public Optional<Task> findById(Long aLong) {
        for(int i=0; i< this.tasks.size(); i++) {
            Task task = this.tasks.get(i);
            if(task.id == aLong) return Optional.of(task);
        }

        return Optional.empty();
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param sort
     * @return
     */
    @Override
    public List<Task> findAll(Sort sort) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<Task> findAll(Pageable pageable) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<Task> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @return
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * Deletes the task with the certain ID
     * @param taskID id of the task
     */
    @Override
    public void deleteById(Long taskID) {
        for(Task task : tasks){
            if(task.id == taskID){
                tasks.remove(task);
                return;
            }
        }
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(Task entity) {

    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param longs must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAll(Iterable<? extends Task> entities) {

    }

    /**
     * Required to implement the interface;
     * currently unused
     */
    @Override
    public void deleteAll() {

    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Task> List<S> saveAll(Iterable<S> entities) {
        return null;
    }


    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    /**
     * Required to implement the interface;
     * currently unused
     */
    @Override
    public void flush() {

    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param entity entity to be saved. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Task> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param entities entities to be saved. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<Task> entities) {

    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param longs the ids of the entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     * Required to implement the interface;
     * currently unused
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Task getOne(Long aLong) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Task getById(Long aLong) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param example must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Task> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param example must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Task> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param example must not be {@literal null}.
     * @param sort the {@link Sort} specification to sort the results by,
     * must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param example must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Task> long count(Example<S> example) {
        return 0;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param example the {@link Example} to use for the existence check.
     * Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Task> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * Required to implement the interface;
     * currently unused
     *
     * @param example must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @return
     * @param <S>
     * @param <R>
     */
    @Override
    public <S extends Task, R> R findBy
    (
            Example<S> example,
            Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction
    ) {
        return null;
    }
    /** Return all tasks belonging to a certain card
     * @param cardId id of the card
     * @return all the tasks belonging
     */
    @Override
    public List<Task> findAllTasksByCardId(long cardId) {
        List<Task> tasksByCardId = new ArrayList<>();
        for(Task task : tasks){
            if(task.card.id == cardId) tasksByCardId.add(task);
        }
        return tasksByCardId;
    }

    /**
     * Imitates the query that fetches the number of complete tasks in a card
     * @param cardId card on which the tasks reside
     * @return Integer : number of _complete_ tasks
     */
    @Override
    public Integer getNumCompleted(long cardId) {
        int counter = 0;
        for(Task task : this.tasks) {
            if(task.completed) counter++;
        }
        return counter;
    }

    /**
     * Imitates the query that fetches the number of all tasks in a card
     * @param cardId card on which the tasks reside
     * @return Integer : number of tasks in a card
     */
    @Override
    public Integer getNumAll(long cardId) {
        return this.tasks.size();
    }

    /**
     * Imitates the task rank incrementing method
     * @param cardID ID of card on which tasks reside
     * @param rank minimal rank being updated (all above or equal incremented)
     * @return
     */
    @Override
    public int incrementAllTaskRanksGTERank(long cardID, long rank) {
        int modifiedCount = 0;
        for(int i=0; i<this.tasks.size(); i++) {
            Task task = this.tasks.get(i);
            if(task.card.id == cardID && task.rank >= rank) {
                this.tasks.get(i).rank++;
                modifiedCount++;
            }
        }
        return modifiedCount;
    }

    /**
     * Imitates the query that retrieves all tasks other than a given one
     * @param cardID ID of card on which the tasks are
     * @param excludedTaskID ID of task that must be excluded
     * @return
     */
    @Override
    public List<Task> getOtherTasks(long cardID, long excludedTaskID) {
        List<Task> sorted = new ArrayList<>(this.tasks.size());
        sorted.addAll(this.tasks);

        Collections.sort(sorted, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return Long.compare(task1.rank, task2.rank);
            }
        });

        List<Task> found = new ArrayList<>();
        for(int i=0; i<sorted.size(); i++) {
            Task task = sorted.get(i);
            if(task.card.id == cardID && task.id != excludedTaskID) {
                found.add(task);
            }
        }
        return found;
    }
}
