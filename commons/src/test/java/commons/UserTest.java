package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    public User user;

    public Board board;

    @BeforeEach
    public void setupEnv() {
        User user = new User();
        user.id = 1;
        user.username = "testUser";
        user.password = "testPassword";
        this.user = user;

        Board board = new Board();
        board.title = "Board Title";
        board.id = 1;
        this.board = board;
    }

    @Test
    @DisplayName("empty constructor works correctly")
    public void emptyConstructorTest() {
        User testUser = new User();
        assertNull(testUser.username);
    }

    @Test
    @DisplayName("correctly sets joinedBoards")
    public void setJoinedBoardsTest() {
        this.user.joinedBoards.add(this.board);
        assertTrue(this.user.joinedBoards.contains(this.board));
    }

    @Test
    @DisplayName("correctly sets username")
    public void setUsernameTest() {
        this.user.username = "testUsername";
        assertEquals("testUsername", this.user.username);
    }

    @Test
    @DisplayName("equals method [true positive]")
    public void equalsTestPositive() {
        User testUser = new User("testUser", "testPassword");
        testUser.id = 1L;

        assertEquals(this.user, testUser);
    }

    @Test
    @DisplayName("equals method [true negative] : ID")
    public void equalsTestNegativeID() {
        User testUser = new User("testUser", "testPassword");
        testUser.id = 2L;

        assertNotEquals(this.user, testUser);
    }

    @Test
    @DisplayName("equals method [true negative] : name")
    public void equalsTestNegativeName() {
        User testUser = new User("testUser2", "testPassword");
        testUser.id = 1L;

        assertNotEquals(this.user, testUser);
    }

    @Test
    @DisplayName("correctly computes hashcode")
    public void hashTestSingle() {
        int expected = Objects.hash(this.user.id, this.user.username, this.user.joinedBoards, this.user.password);
        assertEquals(this.user.hashCode(), expected);
    }

    @Test
    @DisplayName("equal cards have same hashcode")
    public void hashTwoCards() {
        User testUser = new User("testUser", "testPassword");
        testUser.id = 1L;

        assertEquals(this.user.hashCode(), testUser.hashCode());
    }

    @Test
    @DisplayName("correctly creates toString")
    public void toStringTest() {
        String expected = "User{id=1, username='testUser', joinedBoards=[], password='testPassword'}";
        assertEquals(this.user.toString(), expected);
    }

 }
