import commons.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DummyTagRepository implements TagRepository {
    public List<Tag> tags = new ArrayList<>();

    /**
     * save replacement for the test database
     *
     * @param entity must not be {@literal null}.
     * @param <S>
     * @return Tag entity that was saved
     */
    @Override
    public <S extends Tag> S save(S entity) {
        for (Tag tag : tags) {
            if (tag.id == entity.id) {
                tags.remove(tag);
                tags.add(entity);
                return entity;
            }
        }
        if (this.tags.contains(entity)) {
            int position = this.tags.indexOf(entity);
            this.tags.set(position, entity);
        } else {
            this.tags.add(entity);
        }
        return entity;
    }

    /**
     * findById replacement for the test database
     *
     * @return the Tag searched for
     */
    @Override
    public Optional<Tag> findById(Long aLong) {
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            if (tag.id == aLong) return Optional.of(tag);
        }

        return Optional.empty();
    }

    /**
     * @return List of all Tags from the database
     */
    @Override
    public List<Tag> findAll() {
        return this.tags;
    }

    /**
     * The methods below must be implemented due to TagRepository interface constraints,
     * * but currently do not provide any actual functionality for tests
     **/
    @Override
    public List<Tag> findAll(Sort sort) {
        return null;
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return null;
    }

    /**
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<Tag> findAllById(Iterable<Long> longs) {
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
     * @param tagId the id of the tag to be deleted
     */
    @Override
    public void deleteById(Long tagId) {
        for (Tag tag : tags) {
            if (tag.id == tagId) {
                tags.remove(tag);
                return;
            }
        }
    }

    /**
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(Tag entity) {
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
    public void deleteAll(Iterable<? extends Tag> entities) {
        tags.clear();
    }

    /**
     *
     */
    @Override
    public void deleteAll() {
        tags.clear();
    }

    /**
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * @param tagID must not be {@literal null}.
     * @return
     */
    @Override
    public boolean existsById(Long tagID) {
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
    public <S extends Tag> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {
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
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Tag getOne(Long aLong) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Tag getById(Long aLong) {
        return null;
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
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
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Tag> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example the {@link Example} to use for the existence check.
     *                Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
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
    public <S extends Tag, R> R findBy
    (Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
