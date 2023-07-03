import commons.Card;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.CardRepository;

import java.util.*;
import java.util.function.Function;

/**
 * This dummy repository allows testing methods from AppController
 * that require the use of some DB.
 *
 * This class provides emulation of a runtime DB-like system that can
 * imitate this kind of functionality by implementing the required interface.
 *
 * Later, in tests, this class can be used in place of the original
 * CardRepository interface.
 */
public class DummyCardRepository implements CardRepository {
    List<Card> cards = new ArrayList<>();

    /**
     * Imitates the `findAll` implemented by the Jpa Repository
     * @return all cards in repository
     */
    @Override
    public List<Card> findAll() {
        return this.cards;
    }

    /**
     * Imitates the `save` implemented by the Jpa Repository
     * @param entity must not be {@literal null}.
     * @return the entity which was input
     * @param <S> entity which is being modified
     */
    @Override
    public <S extends Card> S save(S entity) {
        boolean updated = false;
        for(Card card : this.cards) {
            if(card.id == entity.id) {
                int position = this.cards.indexOf(card);
                this.cards.set(position, entity);
                updated = true;
            }
        }

        if(!updated) {
            this.cards.add(entity);
        }

        return entity;
    }

    /**
     * Imitates the `findByID` implemented by the Jpa Repository
     * @param aLong must not be {@literal null}.
     * @return Optional of Card, if found
     */
    @Override
    public Optional<Card> findById(Long aLong) {
        for(int i=0; i< cards.size(); i++) {
            Card card = cards.get(i);
            if(card.id == aLong) return Optional.of(card);
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
    public List<Card> findAll(Sort sort) {
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
    public List<Card> findAllById(Iterable<Long> longs) {
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
    public Page<Card> findAll(Pageable pageable) {
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
     * Deletes the card with specific id
     * @param cardId the id of the card to be deleted.
     */
    @Override
    public void deleteById(Long cardId) {
        for(Card card : cards){
            if(card.id == cardId){
                cards.remove(card);
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
    public void delete(Card entity) {

    }

    /**
     * Required to implement the interface;
     * currently unused
     *
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
    public void deleteAll(Iterable<? extends Card> entities) {

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
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Card> List<S> saveAll(Iterable<S> entities) {
        return null;
    }


    /**
     * Required to implement the interface;
     * currently unused
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
     * @param entity entity to be saved. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Card> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     * @param entities entities to be saved. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Card> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<Card> entities) {

    }

    /**
     * Required to implement the interface;
     * currently unused
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
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Card getOne(Long aLong) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     * @param cardID must not be {@literal null}.
     * @return
     */
    @Override
    public Card getById(Long cardID) {
        for(Card card : this.cards) {
            if(card.id == (long) cardID) return card;
        }
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     * @param example must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Card> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * Required to implement the interface;
     * currently unused
     * @param example must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Card> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     * @param example must not be {@literal null}.
     * @param sort the {@link Sort} specification to sort the results by,
     * must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Card> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     * @param example must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Card> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * Required to implement the interface;
     * currently unused
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Card> long count(Example<S> example) {
        return 0;
    }

    /**
     * Required to implement the interface;
     * currently unused
     * @param example the {@link Example} to use for the existence check.
     * Must not be {@literal null}.
     * @return
     * @param <S>
     */
    @Override
    public <S extends Card> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * Required to implement the interface;
     * currently unused
     * @param example must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @return
     * @param <S>
     * @param <R>
     */
    @Override
    public <S extends Card, R> R findBy(
            Example<S> example,
            Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction
    ) {
        return null;
    }

    /** Return all cards belonging to a certain list
     * @param listId id of the board
     * @return all the lists belonging
     */
    @Override
    public List<Card> findAllCardsByListId(long listId){
        List<Card> cardsFromList = new ArrayList<>();
        for (Card card : cards){
            if(card.list.id == listId){
                cardsFromList.add(card);
            }
        }

        return cardsFromList;
    }

    /**
     * Imitates the `findAllCardsAtHigherRankThan` query in CardRepository.
     * Finds all cards with rank >= the rank specified
     *
     * @param listId ID of list on which the cards are to be updated
     * @param rank minimal rank that needs to be found
     * @return list of cards that match the parameters
     */
    @Override
    public List<Card> findAllCardsAtHigherRankThan(long listId, long rank) {
        List<Card> returnedCards = new ArrayList<>();
        for (Card card :
                this.cards) {
            if (card.list.id == listId && card.rank >= rank) {
                returnedCards.add(card);
            }
        }

        return returnedCards;
    }

    /**
     * Imitates the `updateAllCardRanksGTERank` query in CardRepository
     * Updates all cards with rank >= the rank specified
     *
     * @param listId ID of list on which the cards are to be updated
     * @param rank minimal rank that needs to be updated
     * @return integer specifying what is the minimal rank affected
     */
    @Override
    public int incrementAllCardRanksGTERank(long listId, long rank) {
        for(int i=0; i<this.cards.size(); i++) {
            Card card = this.cards.get(i);
            if(card.list.id == listId && card.rank >= rank) {
                this.cards.get(i).rank++;
            }
        }
        return (int) rank;
    }

    /**
     * Imitates the `getOtherCards` query in CardRepository
     * Fetches all cards different from with specified ID,
     * in rank order
     *
     * @param listID ID of list on which the cards are
     * @param excludedCardID ID of card that must be excluded
     * @return
     */
    @Override
    public List<Card> getOtherCards(long listID, long excludedCardID) {
        List<Card> sorted = new ArrayList<>();
        Collections.copy(this.cards, sorted);

        Collections.sort(sorted, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                return Long.compare(card1.rank, card2.rank);
            }
        });

        List<Card> found = new ArrayList<>();
        for(int i=0; i<sorted.size(); i++) {
            Card card = sorted.get(i);
            if(card.list.id == listID && card.id != excludedCardID) {
                found.add(card);
            }
        }
        return found;
    }
}
