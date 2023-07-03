package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class BoardTest {
    Board board;
    @BeforeEach
    public void setBoard() {
        this.board = new Board();
        board.id = 2;
        board.title = "Board Title 1";
    }

    @Test
    @DisplayName("empty constructor test")
    public void emptyConstructorTest() {
        Board testBoard = new Board();

        assertEquals(testBoard.id, 0);
        assertNull(testBoard.title);
    }

    @Test
    @DisplayName("equals() method [true positive]")
    public void equalsTestPositive() {
        Board secondBoard = new Board();
        secondBoard.id = 2;
        secondBoard.title = "Board Title 1";

        assertEquals(board, secondBoard);
    }

    @Test
    @DisplayName("equals() method [true negative]: ID")
    public void equalsTestNegativeID() {
        Board secondBoard = new Board();
        secondBoard.id = 1000;
        secondBoard.title = "Board Title 1";

        assertNotEquals(board, secondBoard);
    }

    @Test
    @DisplayName("equals() method [true negative]: Title")
    public void equalsTestNegativeTitle() {
        Board secondBoard = new Board();
        secondBoard.id = 1;
        secondBoard.title = "wrong title";

        assertNotEquals(board, secondBoard);
    }

    @Test
    @DisplayName("correctly computes hashcode")
    public void hashTestSingle() {
        int expected =
                ((2 >>> 32) * 31 + "Board Title 1".hashCode());

        assertEquals(board.hashCode(), expected);
    }

    @Test
    @DisplayName("equal boards have the same hashcode")
    public void hashTestTwoBoards() {
        Board secondBoard = new Board();
        secondBoard.id = 2;
        secondBoard.title = "Board Title 1";

        assertEquals(board.hashCode(), secondBoard.hashCode());
    }

    @Test
    @DisplayName("toString produces correct output")
    public void toStringTest() {
        String expected = "Board{id=2, title='Board Title 1'}";
        assertEquals(expected, board.toString());
    }

    @Test
    @DisplayName("correctly adds tag")
    public void addTagTest() {
        Tag tag = new Tag();
        tag.id = 1L;
        tag.name = "tag1";
        board.addTag(tag);
        assertTrue(board.tags.contains(tag));
    }

    @Test
    @DisplayName("correctly removes tag")
    public void removeTagTest() {
        Tag tag = new Tag();
        tag.id = 1L;
        tag.name = "tag1";
        board.addTag(tag);
        board.removeTag(tag);
        assertFalse(board.tags.contains(tag));
    }
}
