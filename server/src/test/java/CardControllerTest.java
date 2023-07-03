import commons.Board;
import commons.Card;
import commons.ListEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AppController;
import server.CardController;
import server.ListController;
import server.RandomPassword;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardControllerTest {

    private AppController appController;
    private ListController listController;

    private CardController cardController;
    private DummyBoardRepository boardRepository;
    private DummyListRepository listRepository;
    private DummyCardRepository cardRepository;

    private RandomPassword randomPassword;

    /**
     * Before each test, sets up a clean repository
     */
    @BeforeEach
    private void setupTestingEnv() throws FileNotFoundException {
        // setup dummy repositories
        this.boardRepository = new DummyBoardRepository();
        this.listRepository = new DummyListRepository();
        this.cardRepository = new DummyCardRepository();

        // setup app controller for tests
        this.appController = new AppController(
                this.boardRepository,
                this.listRepository,
                this.cardRepository,
                null,
                null,
                null,
                randomPassword
        );

        this.listController = new ListController(
                this.listRepository,
                this.cardRepository,
                this.boardRepository
        );

        this.cardController = new CardController(cardRepository, listRepository, listController);

        setupHelper();
    }

    /**
     * Helper method that is called during the setup
     */
    private void setupHelper() throws FileNotFoundException {
        // create two boards for testing
        addDefaultBoard();

        // create two lists in board 1
        addDefaultList("1");

    }

    /**
     * Helper for creating a new default Board for testing
     * @throws FileNotFoundException when files not found
     */
    private void addDefaultBoard() throws FileNotFoundException {
        Board board = new Board();
        board.id = 1;
        board.title = "board1";

        this.appController.addBoard(board);
    }

    /**
     * Helper for creating a new default List for testing
     * @param boardID ID of board on which the list is being created
     * @throws FileNotFoundException when files not found
     */
    private void addDefaultList(String boardID) throws FileNotFoundException {
        ListEntity list1 = new ListEntity();
        list1.id = 1;
        list1.name = "list1";

        ListEntity list2 = new ListEntity();
        list2.id = 2;
        list2.name = "list2";
        this.appController.addList(list1, boardID);
        this.appController.addList(list2, boardID);
    }

    /**
     * Helper for creating a new default Card for testing
     * @param listID ID of list on which the card is being created
     * @throws FileNotFoundException when files not found
     */
    private void addFirstDefaultCard(long listID) throws FileNotFoundException {
        Card card = new Card();
        card.id = 1;
        card.list = this.appController.getListByID(listID);
        card.name = "first card";
        this.appController.addCard(card);
    }

    /**
     * Helper for creating a new default Card for testing
     * @param listID ID of list on which the card is being created
     * @throws FileNotFoundException when files not found
     */
    private void addSecondDefaultCard(long listID) throws FileNotFoundException {
        Card card = new Card();
        card.id = 2;
        card.list = this.appController.getListByID(listID);
        card.name = "second card";
        this.appController.addCard(card);
    }

    /**
     * Tests fetching all cards from repository;
     * Here, the test is run on repo without any cards
     */
    @DisplayName("Fetches all cards correctly when empty")
    @Test
    public void correctlyFetchesCardsEmpty() {
        assertEquals(new ArrayList<Card>(), this.appController.getAllCards());
    }

    /**
     * Tests creating a card on a unique list (ID=1)
     * @throws FileNotFoundException if files missing
     */
    @DisplayName("Correctly creates card 1 on list 1")
    @Test
    public void correctlyCreatesCard1() throws FileNotFoundException {
        addFirstDefaultCard( 1L);

        Card card = new Card();
        card.id = 1L;
        card.name = "first card";
        card.list = this.appController.getListByID(1L);
        card.rank = 0L;

        assertEquals(card, this.cardRepository.cards.get(0));
    }

    /**
     * Tests creating a card on a unique list (ID=2)
     * @throws FileNotFoundException if files missing
     */
    @DisplayName("Correctly creates card 2 on list 2")
    @Test
    public void correctlyCreatesCard2() throws FileNotFoundException {
        addSecondDefaultCard( 2L);

        Card card = new Card();
        card.id = 2L;
        card.name = "second card";
        card.list = this.appController.getListByID(2L);
        card.rank = 0L;

        assertEquals(card, this.cardRepository.cards.get(0));
    }

    /**
     * Tests if the cards were added successfully to the repo
     * @throws FileNotFoundException if files missing
     */
    @DisplayName("Fetches all cards correctly when two added")
    @Test
    public void correctlyFetchesCardsPopulated() throws FileNotFoundException {
        addFirstDefaultCard( 1L);
        addSecondDefaultCard(2L);

        Card card1 = new Card();
        card1.id = 1L;
        card1.name = "first card";
        card1.list = this.appController.getListByID(1L);
        card1.rank = 0L;

        Card card2 = new Card();
        card2.id = 2L;
        card2.name = "second card";
        card2.list = this.appController.getListByID(2L);
        card2.rank = 0L;

        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);

        assertEquals(cardList, this.appController.getAllCards());
    }

    /**
     * Tests if card can be fetched with AppController by selected ID
     * @throws FileNotFoundException if files missing
     */
    @DisplayName("Fetches card by ID")
    @Test
    public void correctlyFetchesCardByID() throws FileNotFoundException {
        addSecondDefaultCard( 2L);

        Card card = new Card();
        card.id = 2L;
        card.name = "second card";
        card.list = this.appController.getListByID(2L);
        card.rank = 0;

        assertEquals(card, this.appController.getCardByID(2));
    }

    @DisplayName("Updates the rank for one card")
    @Test
    public void correctlyUpdateRankOneCard() throws FileNotFoundException {
        addFirstDefaultCard( 1L);

        this.cardController.incrementRanks(1L, 0L);

        Card card = new Card();
        card.id = 1L;
        card.name = "first card";
        card.list = this.appController.getListByID(1L);
        card.rank = 1L;

        assertEquals(this.cardRepository.cards.get(0), card);
    }

    @DisplayName("Updates the rank for multiple cards on one list")
    @Test
    public void correctlyUpdateRankMultiCards() throws FileNotFoundException {
        addFirstDefaultCard(1L);

        // bump up ranks before adding 2nd card
        this.cardController.incrementRanks(1L, 0L);

        addSecondDefaultCard( 1L);

        this.cardController.incrementRanks(1L, 0L);

        Card card1 = new Card();
        card1.id = 1L;
        card1.name = "first card";
        card1.list = this.appController.getListByID(1L);
        card1.rank = 3L;    // this is because `AddCard` also auto-increments ranks

        Card card2 = new Card();
        card2.id = 2L;
        card2.name = "second card";
        card2.list = this.appController.getListByID(1L);
        card2.rank = 1L;

        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);

        assertEquals(cardList, this.cardRepository.cards);
    }

    @DisplayName("Does not update cards that have rank lower than specified")
    @Test
    public void leavesLesserRanksUnmodified() throws FileNotFoundException {

        addFirstDefaultCard(1L);
        addSecondDefaultCard(1L);

        this.cardController.incrementRanks(1L, 1L);


        Card card1 = new Card();
        card1.id = 1L;
        card1.name = "first card";
        card1.list = this.appController.getListByID(1L);
        card1.rank = 2L;

        Card card2 = new Card();
        card2.id = 2L;
        card2.name = "second card";
        card2.list = this.appController.getListByID(1L);
        card2.rank = 0L;

        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);

        assertEquals(cardList, this.cardRepository.cards);
    }

    @DisplayName("Automatically increments ranks before card addition")
    @Test
    public void autoIncrementsRanks() throws FileNotFoundException {
        addFirstDefaultCard( 1L);

        long rankPreAdd = this.cardRepository.cards.get(0).rank;

        addSecondDefaultCard( 1L);

        long rankPostAdd = this.cardRepository.cards.get(0).rank;

        assertEquals(rankPostAdd, rankPreAdd + 1);
    }


    /**
     * Tests if correctly edits card
     * @throws FileNotFoundException
     */
    @DisplayName("Edits card")
    @Test
    public void correctlyEditsCards() throws FileNotFoundException {
        addFirstDefaultCard(1L);

        Card expected = new Card();
        expected.id = 1;
        expected.name = "edited";
        expected.list = this.appController.getListByID(1L);
        expected.rank = 0;

        // get the update list
        this.appController.editCard(expected);
        Card newCard = this.appController.getCardByID(1);

        assertEquals(expected, newCard);
    }

    /**
     * Tests if correctly deletes card by id
     * @throws FileNotFoundException
     */
    @DisplayName("Delete card by id")
    @Test
    public void correctlyDeletesCard() throws FileNotFoundException {
        addFirstDefaultCard(1L);

        assertNotNull(this.appController.getCardByID(1L));

        // delete the card
        this.appController.removeCard(this.appController.getCardByID(1L));
        Card deletedCard = this.appController.getCardByID(1L);

        // assert
        assertNull(deletedCard);
    }

    /**
     * Tests if all cards from a certain list can be fetched
     * @throws FileNotFoundException
     */
    @DisplayName("Fetches cards by list ID")
    @Test
    public void correctlyFetchesCardsByListId() throws FileNotFoundException {
        addFirstDefaultCard(1L);
        addSecondDefaultCard(1L);

        List<Card> cardsInList1 = new ArrayList<>();

        Card card1 = new Card();
        card1.id = 1L;
        card1.name = "first card";
        card1.list = this.appController.getListByID(1L);
        card1.rank = 1;

        cardsInList1.add(card1);
        Card card2 = new Card();
        card2.id = 2L;
        card2.name = "second card";
        card2.list = this.appController.getListByID(1L);
        card2.rank = 0;

        cardsInList1.add(card2);

        assertEquals(cardsInList1, this.appController.getCardsByListId(1L));
    }
}