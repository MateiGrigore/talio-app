import commons.ListEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ListRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DummyListRepository  implements ListRepository {
    // set up new list to serve as a dummy repository
    public List<ListEntity> lists = new ArrayList<>();

    /**
     * findAll replacement for the test database
     * @return List of all Lists from the database
     */
    @Override
    public List<ListEntity> findAll() {
        return this.lists;
    }

    /** findAll lists belonging to a certain board
     * @param boardId id of the board
     * @return the lists
     */
    @Override
    public List<ListEntity> findAllListsByBoardId(long boardId){
        return lists.stream().filter(l -> l.board.id == boardId)
                .collect(Collectors.toList());
    }

    /**
     * @param boardId ID of board on which the search is executed
     * @return all lists in the board, ranked ascending by ID
     */
    @Override
    public List<ListEntity> findAllListsByBoardIdDesc(long boardId) {
        var listFromBoard =  lists.stream().filter(l -> l.board.id == boardId)
                .collect(Collectors.toList());

        Collections.sort(listFromBoard, new Comparator<ListEntity>() {
            @Override
            public int compare(ListEntity list1, ListEntity list2) {
                return Long.compare(list1.id, list2.id);
            }
        });

        return listFromBoard;
    }

    /**
     * save replacement for the test database
     * @param entity must not be {@literal null}.
     * @return List entity that was saved
     * @param <S>
     */
    @Override
    public <S extends ListEntity> S save(S entity) {
        // ID should be unique; thus find if ID exists
        boolean updated = false;
        for(ListEntity list : this.lists) {
            if(list.id == entity.id) {
                int position = this.lists.indexOf(list);
                this.lists.set(position, entity);
                updated = true;
            }
        }

        if(!updated) {
            this.lists.add(entity);
        }

        return entity;
    }

    /**
     * findById replacement for the test database
     * @param aLong the id of the List
     * @return the List searched for
     */
    @Override
    public Optional<ListEntity> findById(Long aLong) {
        for(int i=0; i< lists.size(); i++) {
            ListEntity list = lists.get(i);
            if(list.id == aLong) return Optional.of(list);
        }

        return Optional.empty();
    }
    /** The methods below must be implemented due to ListRepository interface constraints,
     ** but currently do not provide any actual functionality for tests
     * **/
    @Override
    public List<ListEntity> findAll(Sort sort) { return null; }

    /**
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<ListEntity> findAll(Pageable pageable) { return null; }

    /**
     *
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<ListEntity> findAllById(Iterable<Long> longs) { return null; }

    /**
     *
     * @return
     */
    @Override
    public long count() { return 0; }

    /**
     * deleteById replacement for the test database
     * @param aLong the id of the List
     */
    @Override
    public void deleteById(Long aLong) {
        for(ListEntity listEntity : lists){
            if(listEntity.id == aLong){
                lists.remove(listEntity);
                return;
            }
        }
    }

    /**
     *
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(ListEntity entity) { }

    /**
     *
     * @param longs must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) { }

    /**
     *
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAll(Iterable<? extends ListEntity> entities) { }

    /**
     *
     */
    @Override
    public void deleteAll() { }

    /**
     *
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends ListEntity> List<S> saveAll(Iterable<S> entities) { return null; }

    /**
     *
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public boolean existsById(Long aLong) { return false; }

    /**
     *
     */
    @Override
    public void flush() { }

    /**
     *
     * @param entity entity to be saved. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends ListEntity> S saveAndFlush(S entity) { return null; }

    /**
     *
     * @param entities entities to be saved. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends ListEntity> List<S> saveAllAndFlush(Iterable<S> entities) { return null; }

    /**
     *
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<ListEntity> entities) { }

    /**
     *
     * @param longs the ids of the entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) { }

    /**
     *
     */
    @Override
    public void deleteAllInBatch() { }

    /**
     *
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public ListEntity getOne(Long aLong) { return null; }

    /**
     *
     * @param listID must not be {@literal null}.
     * @return
     */
    @Override
    public ListEntity getById(Long listID) {
        for(ListEntity list : this.lists) {
            if(list.id == (long) listID) return list;
        }
        return null;
    }

    /**
     *
     * @param example must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends ListEntity> Optional<S> findOne(Example<S> example)
    {
        return Optional.empty();
    }

    /**
     *
     * @param example must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends ListEntity> List<S> findAll(Example<S> example) { return null; }

    /**
     *
     * @param example must not be {@literal null}.
     * @param sort the {@link Sort}
     * specification to sort the results by, must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends ListEntity> List<S> findAll(Example<S> example, Sort sort) { return null; }

    /**
     *
     * @param example must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends ListEntity> Page<S> findAll(Example<S> example, Pageable pageable)
    {
        return null;
    }

    /**
     *
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends ListEntity> long count(Example<S> example) { return 0; }

    /**
     * @param example the {@link Example} to use for the existence check.
     *                Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends ListEntity> boolean exists(Example<S> example) { return false; }

    /**
     *
     * @param example must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @return
     * @param <S>
     * @param <R>
     */
    @Override
    public <S extends ListEntity, R> R findBy
    (Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction)
    { return null; }
}
