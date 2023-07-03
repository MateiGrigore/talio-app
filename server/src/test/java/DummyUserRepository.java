import commons.Board;
import commons.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DummyUserRepository implements UserRepository {

    // set up new list to serve as a dummy repository
    public List<User> users = new ArrayList<>();


    /**
     * findAll replacement for the test database
     *
     * @return List of all Boards from the database
     */
    @Override
    public List<User> findAll() {
        return this.users;
    }

    /**
     * save replacement for the test database
     *
     * @param entity must not be {@literal null}.
     * @param <S>
     * @return Board entity that was saved
     */
    @Override
    public <S extends User> S save(S entity) {
        if (this.users.contains(entity)) {
            int position = this.users.indexOf(entity);
            this.users.set(position, entity);
        } else {
            this.users.add(entity);
        }
        return entity;
    }

    /**
     * findById replacement for the test database
     *
     * @return the Board searched for
     */
    @Override
    public Optional<User> findById(Long userId
    ) {
        for (int i = 0; i < users.size(); i++) {
            User board = users.get(i);
            if (board.id == userId
            ) return Optional.of(board);
        }

        return Optional.empty();
    }

    /**
     * The methods below must be implemented due to BoardRepository interface constraints,
     * * but currently do not provide any actual functionality for tests
     **/
    @Override
    public List<User> findAll(Sort sort) {
        return null;
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    /**
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<User> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * @param userId must not be {@literal null}.
     */
    @Override
    public void deleteById(Long userId
    ) {
    }

    /**
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(User entity) {
    }

    /**
     * @param longs must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
    }

    /**
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAll(Iterable<? extends User> entities) {
    }

    /**
     *
     */
    @Override
    public void deleteAll() {
    }

    /**
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * @param userId must not be {@literal null}.
     * @return
     */
    @Override
    public boolean existsById(Long userId
    ) {
        return false;
    }

    /**
     *
     */
    @Override
    public void flush() {
    }

    /**
     * @param entity entity to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<User> entities) {
    }

    /**
     * @param longs the ids of the entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
    }

    /**
     *
     */
    @Override
    public void deleteAllInBatch() {
    }

    /**
     * @param userId must not be {@literal null}.
     * @return
     */
    @Override
    public User getOne(Long userId
    ) {
        return null;
    }

    /**
     * @param userId must not be {@literal null}.
     * @return
     */
    @Override
    public User getById(Long userId
    ) {
        return null;
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * @param example must not be {@literal null}.
     * @param sort    the {@link Sort}
     *                specification to sort the results by, must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example the {@link Example} to use for the existence check.
     *                Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * @param example       must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @param <S>
     * @param <R>
     * @return
     */
    @Override
    public <S extends User, R> R findBy
    (Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    /**
     * @param username username of the user to find
     * @return the user with the given username
     */
    @Override
    public List<Board> findAllBoardsByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return new ArrayList<>(user.getJoinedBoards());
            }
        }
        return List.of();
    }

    /**
     * @param username username of the user to find
     * @return the user with the given username
     */
    @Override
    public Optional<User> findByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}