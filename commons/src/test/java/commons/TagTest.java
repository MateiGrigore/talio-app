package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {
    public Tag tag;

    public Board board;

    @BeforeEach
    public void setupEnv() {
        this.board = new Board();
        this.board.id = 1L;
        this.board.title = "Board Title";

        this.tag = new Tag();
        this.tag.name = "Tag with ID=1";
        this.tag.id = 1L;
        tag.r = 0;
        tag.g = 0;
        tag.b = 0;
    }

    @Test
    @DisplayName("empty constructor works correctly")
    public void emptyConstructorTest() {
        Tag testTag = new Tag();

        assertNull(testTag.name);
    }

    @Test
    @DisplayName("board field null if never set")
    public void nullTagLinker() {
        assertNull(this.tag.board);
    }

    @Test
    @DisplayName("correctly sets Board")
    public void setBoardTest() {
        // call setBoard
        this.tag.setBoard(this.board);
        // check if correctly set
        assertSame(this.tag.board, this.board); // must be the same instance
    }

    @Test
    @DisplayName("equals method [true positive]")
    public void equalsTestPositive() {
        Tag testTag = new Tag("Tag with ID=1", 0, 0, 0);
        testTag.id = 1L;

        assertEquals(testTag, this.tag);
    }

    @Test
    @DisplayName("equals method [true negative] : ID")
    public void equalsTestNegativeID() {
        Tag testTag = new Tag();
        testTag.name = "Tag with ID=1";
        testTag.id = 3L;
        testTag.setBoard(this.board);
        testTag.r = 0;
        testTag.g = 0;
        testTag.b = 0;

        assertNotEquals(testTag, this.tag);
    }

    @Test
    @DisplayName("equals method [true negative] : name")
    public void equalsTestNegativeName() {
        Tag testTag = new Tag();
        testTag.name = "Tag with ID=2";
        testTag.id = 1L;
        testTag.setBoard(this.board);
        testTag.r = 0;
        testTag.g = 0;
        testTag.b = 0;

        assertNotEquals(testTag, this.tag);
    }

    @Test
    @DisplayName("equals method [true negative] : RGB value")
    public void equalsTestNegativeRGB() {
        Tag testTag = new Tag();
        testTag.name = "Tag with ID=2";
        testTag.id = 1L;
        testTag.setBoard(this.board);
        testTag.r = 1;
        testTag.g = 1;
        testTag.b = 1;

        assertNotEquals(testTag, this.tag);
    }

    @Test
    @DisplayName("correctly computes hashcode")
    public void hashTest() {
        // set board in first created tag
        this.tag.setBoard(this.board);

        Tag testTag = new Tag();
        testTag.name = "Tag with ID=1";
        testTag.id = 1L;
        testTag.setBoard(this.board);
        testTag.r = 0;
        testTag.g = 0;
        testTag.b = 0;

        assertEquals(testTag.hashCode(), this.tag.hashCode());
    }

    @Test
    @DisplayName("equal cards have same hashcode")
    public void hashTwoCards() {
        // set board in first created tag
        this.tag.setBoard(this.board);

        Tag testTag = new Tag();
        testTag.name = "Tag with ID=1";
        testTag.id = 1L;
        testTag.setBoard(this.board);
        testTag.r = 0;
        testTag.g = 0;
        testTag.b = 0;

        assertEquals(testTag.hashCode(), this.tag.hashCode());
    }

    @Test
    @DisplayName("correctly creates toString")
    public void toStringTest() {
        // set board in first created tag
        this.tag.setBoard(this.board);

        Tag testTag = new Tag();
        testTag.name = "Tag with ID=1";
        testTag.id = 1L;
        testTag.setBoard(this.board);
        testTag.r = 0;
        testTag.g = 0;
        testTag.b = 0;

        assertEquals(testTag.toString(), this.tag.toString());
    }

}
