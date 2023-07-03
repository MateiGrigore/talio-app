package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    Card card;
    ListEntity list;
    Board board;

    @BeforeEach
    public void setupEnv() {
        // set up board (reachable from List)
        this.board = new Board();
        this.board.id = 1;
        this.board.title = "Board Title";

        // set up list (linked to by card)
        this.list = new ListEntity();
        this.list.id = 1;
        this.list.name = "List 1, for board 1";
        this.list.setBoard(this.board);

        // set up the tested card entity
        this.card = new Card();
        this.card.name = "Card with ID=2";
        this.card.id = 2;
        this.card.rank = 0;
        // leave out setting the list;
        // will be tested in a test
    }

    @Test
    @DisplayName("empty constructor works correctly")
    public void emptyConstructorTest() {
        Card testCard = new Card();

        assertEquals(testCard.id, 0);
        assertNull(testCard.name);
    }

    @Test
    @DisplayName("list field null if never set")
    public void nullListLinker() {
        assertNull(this.card.list);
    }

    @Test
    @DisplayName("correctly sets List")
    public void setListTest() {
        // call setList
        this.card.setList(this.list);
        // check if correctly set
        assertSame(this.card.list, this.list); // must be the same instance
    }

    @Test
    @DisplayName("equals method [true positive]")
    public void equalsTestPositive() {
        // set list in first created card
        this.card.setList(this.list);

        Card testCard = new Card();
        testCard.name = "Card with ID=2";
        testCard.id = 2;
        testCard.setList(this.list);
        testCard.rank = 0;

        assertEquals(testCard, this.card);
    }

    @Test
    @DisplayName("equals method [true negative] : ID")
    public void equalsTestNegativeID() {
        Card testCard = new Card();
        testCard.name = "Card with ID=2";
        testCard.id = 1;
        testCard.setList(this.list);
        testCard.rank = 0;

        assertNotEquals(testCard, this.card);
    }

    @Test
    @DisplayName("equals method [true negative] : name")
    public void equalsTestNegativeName() {
        Card testCard = new Card();
        testCard.name = "Card with wrong name";
        testCard.id = 2;
        testCard.setList(this.list);
        testCard.rank = 0;

        assertNotEquals(testCard, this.card);
    }

    @Test
    @DisplayName("equals method [true negative] : rank")
    public void equalsTestNegativeRank() {
        Card testCard = new Card();
        testCard.name = "Card with ID=2";
        testCard.id = 2;
        testCard.setList(this.list);
        testCard.rank = 1;

        assertNotEquals(testCard, this.card);
    }

    @Test
    @DisplayName("correctly computes hashcode")
    public void hashTestSingle() {
        this.card.setList(this.list);
        int expected = Objects.hash(
                this.card.id,
                this.card.name,
                this.card.list,
                this.card.rank
        );

        assertEquals(this.card.hashCode(), expected);
    }

    @Test
    @DisplayName("equal cards have same hashcode")
    public void hashTwoCards() {
        // set list in first created card
        this.card.setList(this.list);

        Card testCard = new Card();
        testCard.name = "Card with ID=2";
        testCard.id = 2;
        testCard.setList(this.list);

        assertEquals(testCard.hashCode(), this.card.hashCode());
    }

    @Test
    @DisplayName("correctly creates toString")
    public void toStringTest() {
        this.card.setList(this.list);
        String expected =
                "Card{id=2, name='Card with ID=2', rank=0, list=ListEntity{id=1, board=Board{id=1, title='Board Title'}, name='List 1, for board 1'}, description= null}";

        assertEquals(expected, this.card.toString());
    }

}
