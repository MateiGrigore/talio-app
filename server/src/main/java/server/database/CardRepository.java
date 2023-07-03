package server.database;

import commons.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    /** Return all cards belonging to a certain list
     * @param listId id of the board
     * @return all the lists belonging
     */
    @Query("SELECT c FROM Card c WHERE c.list.id = ?1")
    List<Card> findAllCardsByListId(long listId);

    /**
     * Query that returns all cards that have a rank
     * greater than or equal to the one specified
     * @param listId id of the list on which
     * the cards are
     * @param rank specified rank, which determines
     * what cards will be affected
     * @return list of cards that match the query
     */
    @Query("SELECT c FROM Card c WHERE c.list.id = ?1 AND c.rank >= ?2")
    List<Card> findAllCardsAtHigherRankThan(long listId, long rank);

    /**
     * A transaction that updates all cards whose listID matches
     * the one specified in the parameters.
     * Increments the ranks of all cards whose rank is already
     * greater than or equal to a given rank.
     * @param listId id of the list on which
     * the cards are
     * @param rank specified rank, which determines
     * what cards will be affected
     * @return the number of affected tuples
     */
    @Transactional
    @Modifying
    @Query("UPDATE Card c SET c.rank = c.rank+1 WHERE c.list.id = ?1 AND c.rank >= ?2")
    int incrementAllCardRanksGTERank(long listId, long rank);

    /**
     * A query that gets all cards except a card with selected ID
     * @param listID ID of list on which the cards are
     * @param excludedCardID ID of card that must be excluded
     * @return list of cards that match the query
     */
    @Query("SELECT c FROM Card c WHERE c.list.id = ?1 AND c.id != ?2 ORDER BY c.rank ASC")
    List<Card> getOtherCards(long listID, long excludedCardID);

}
