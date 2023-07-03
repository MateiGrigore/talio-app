import commons.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * This dummy repository allows testing the methods
 * from the AppController application.
 *
 * This class defines some generic, test-specific functionality
 * that mimics the way data is stored in the database,
 * which is done by implementing our existing BoardRepository
 * and defining dummy methods for each method required by the interface
 *
 * This way, we can use ApplicationController-specific methods,
 * while not having to rely on a database during testing
 */
public class DummyBoardRepository implements BoardRepository {

    // set up new list to serve as a dummy repository
    public List<Board> boards = new ArrayList<>();

    /**
     * findAll replacement for the test database
     * @return List of all Boards from the database
     */
    @Override
    public List<Board> findAll() {
        return this.boards;
    }

    /**
     * save replacement for the test database
     * @param entity must not be {@literal null}.
     * @return Board entity that was saved
     * @param <S>
     */
    @Override
    public <S extends Board> S save(S entity) {
        for (Board board : boards){
            if(board.id == entity.id){
                boards.remove(board);
                boards.add(entity);
                return entity;
            }
        }
        if(this.boards.contains(entity)) {
            int position = this.boards.indexOf(entity);
            this.boards.set(position, entity);
        } else {
            this.boards.add(entity);
        }
        return entity;
    }

    /**
     * findById replacement for the test database
     * @return the Board searched for
     */
    @Override
    public Optional<Board> findById(Long aLong) {
        for(int i=0; i< boards.size(); i++) {
            Board board = boards.get(i);
            if(board.id == aLong) return Optional.of(board);
        }

        return Optional.empty();
    }
    /** The methods below must be implemented due to BoardRepository interface constraints,
     ** but currently do not provide any actual functionality for tests
     * **/
    @Override
    public List<Board> findAll(Sort sort) { return null; }

    /**
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<Board> findAll(Pageable pageable) { return null; }

    /**
     *
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<Board> findAllById(Iterable<Long> longs) { return null; }

    /**
     *
     * @return
     */
    @Override
    public long count() { return 0; }

    /**
     *
     * @param boardId the id of the board to be deleted
     */
    @Override
    public void deleteById(Long boardId) {
        for (Board board : boards){
            if(board.id == boardId){
                boards.remove(board);
                return;
            }
        }
    }

    /**
     *
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(Board entity) { }

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
    public void deleteAll(Iterable<? extends Board> entities) {
        boards.clear();
    }

    /**
     *
     */
    @Override
    public void deleteAll() {
        boards.clear();
    }

    /**
     *
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Board> List<S> saveAll(Iterable<S> entities) { return null; }

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
    public <S extends Board> S saveAndFlush(S entity) { return null; }

    /**
     *
     * @param entities entities to be saved. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Board> List<S> saveAllAndFlush(Iterable<S> entities) { return null; }

    /**
     *
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<Board> entities) { }

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
    public Board getOne(Long aLong) { return null; }

    /**
     *
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Board getById(Long aLong) { return null; }

    /**
     *
     * @param example must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Board> Optional<S> findOne(Example<S> example) { return Optional.empty(); }

    /**
     *
     * @param example must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Board> List<S> findAll(Example<S> example) { return null; }

    /**
     *
     * @param example must not be {@literal null}.
     * @param sort the {@link Sort}
     * specification to sort the results by, must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Board> List<S> findAll(Example<S> example, Sort sort) { return null; }

    /**
     *
     * @param example must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Board> Page<S> findAll(Example<S> example, Pageable pageable) { return null; }

    /**
     *
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Board> long count(Example<S> example) { return 0; }

    /**
     * @param example the {@link Example} to use for the existence check.
     *                Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Board> boolean exists(Example<S> example) { return false; }

    /**
     *
     * @param example must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @return
     * @param <S>
     * @param <R>
     */
    @Override
    public <S extends Board, R> R findBy
    (Example<S> example,Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction)
    { return null; }
}