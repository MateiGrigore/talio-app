package server;

import commons.Card;
import commons.ListEntity;
import server.database.CardRepository;
import server.database.ListRepository;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CardController {
    private CardRepository cards;
    private ListRepository lists;

    private ListController listController;

    /**
     * @param cards
     * @param lists
     * @param listController
     */
    public CardController(CardRepository cards, ListRepository lists,
                          ListController listController) {
        this.cards = cards;
        this.lists = lists;
        this.listController = listController;
    }

    /**
     * @return All the cards in the database
     */
    public List<Card> getAllCards() {
        return cards.findAll();
    }

    /**
     * @param id The id of the card
     * @return The card with the given id
     */
    public Card getCardByID(long id) {
        return cards.findById(id).orElse(null);
    }

    /**
     * @param listId The id of the list
     * @return All the cards in the given list
     */
    public List<Card> getCardsByListId(long listId) {

        Optional<ListEntity> optionalList = lists.findById(listId);
        if (optionalList.isEmpty()) {
            return new ArrayList<>();
        }

        return cards.findAllCardsByListId(listId);
    }

    /**
     * @param card The card to be added
     * @return True if the card was added successfully, false otherwise
     */
    public boolean addCard(Card card) {
        ListEntity listInDB = listController.getListByID(card.list.id);

        if (listInDB == null) return false;
        incrementRanks(card.list.id, 0);

        card.setList(listInDB);
        cards.save(card);

        return true;
    }

    /**
     * @param card    The card to be moved
     * @param newRank The new rank of the card
     * @return True if the card was moved successfully, false otherwise
     */
    public Card moveCardAndRestructure(Card card, long newRank) {

        // get card that was moved
        Card cardFromRepo = getCardByID(card.id);
        if (cardFromRepo == null) return null;

        long listID = cardFromRepo.list.id;

        // fetch all other cards
        List<Card> other = cards.getOtherCards(listID, cardFromRepo.id);

        // convert to a iterator
        Iterator<Card> iterator = other.iterator();

        int maxSpaces = other.size() + 1;

        Card[] arrCards = new Card[maxSpaces];

        // iterate over positions & add to list
        for (int pos = 0; pos < maxSpaces; pos++) {
            if (pos != newRank && iterator.hasNext()) {
                arrCards[pos] = iterator.next();
            } else {
                arrCards[pos] = cardFromRepo;
            }
        }

        // move each carddummyUserRepositor
        for (int i = 0; i < maxSpaces; i++) {
            arrCards[i].rank = (long) i;
            cards.save(arrCards[i]);
        }

        return card;
    }

    /**
     * @param card The card to be updated
     * @return The updated card
     */
    public Card saveCardOverwrite(Card card) {
        return cards.save(card);
    }

    /**
     * @param card The card to be removed
     * @return True if the card was deleted successfully, false otherwise
     */
    public boolean deleteCard(Card card) {

        Optional<Card> optionalCard = cards.findById(card.id);
        if (optionalCard.isEmpty()) return false;

        cards.deleteById(card.id);

        return true;
    }

    /**
     * Endpoint that updates a Card's ListEntity
     *
     * @param newListID ID of ListEntity affected
     * @param moved     moved Card
     * @return adjusted list
     */
    public ListEntity editCardsList(Card moved, long newListID) {

        long id = moved.id;

        Optional<Card> optionalCard = cards.findById(id);

        if (optionalCard.isEmpty()) {
            return null;
        }

        Optional<ListEntity> optionalList = lists.findById(newListID);

        if (optionalList.isEmpty()) {
            return null;
        }

        Card card = optionalCard.get();
        ListEntity list = optionalList.get();

        // set correct new list
        card.list = list;

        cards.save(card);

        return list;
    }

    /**
     * Squashes cards in the list from which
     * a card was moved from;
     * Solves the issue where moving the first card
     * (or any non-last card) to another list temporarily
     * left empty space
     *
     * @param listID ID of list affected
     * @return updated list
     */
    public ListEntity squashRanks(long listID) {

        // fetch all cards
        List<Card> fetchedCards = cards.getOtherCards(listID, -1);
        int maxSpaces = fetchedCards.size();

        // convert to a iterator
        Iterator<Card> iterator = fetchedCards.iterator();

        Card[] arrCards = new Card[maxSpaces];

        // iterate over positions & add to list
        for (int pos = 0; pos < maxSpaces; pos++) {
            arrCards[pos] = iterator.next();
        }

        // move each card
        for (int i = 0; i < maxSpaces; i++) {
            arrCards[i].rank = (long) i;
            cards.save(arrCards[i]);
        }

        if (arrCards.length > 0) return arrCards[0].list;
        else return null;
    }
    /**
     * @param listId The id of the list
     * @param rank   The rank of the card
     */
    public void incrementRanks(long listId, long rank) {
        this.cards.incrementAllCardRanksGTERank(listId, rank);
    }

}
