import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.BoardController;
import server.UserController;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private DummyBoardRepository dummyBoardRepository;
    private DummyListRepository dummyListRepository;
    private DummyCardRepository dummyCardRepository;
    private DummyTaskRepository dummyTaskRepository;

    private DummyUserRepository dummyUserRepository;
    private BoardController boardController;
    private UserController userController;

    @BeforeEach
    public void setupTestingEnv() {
        // setup dummy repositories
        this.dummyBoardRepository = new DummyBoardRepository();
        this.dummyListRepository = new DummyListRepository();
        this.dummyCardRepository = new DummyCardRepository();
        this.dummyTaskRepository = new DummyTaskRepository();
        this.dummyUserRepository = new DummyUserRepository();

        // setup database controllers
        this.boardController = new BoardController(this.dummyBoardRepository);
        this.userController = new UserController(this.dummyUserRepository);


        setupHelper();
    }

    private void setupHelper() {
        // create two boards for testing
        addSetupBoard(1, "first board");
        addSetupBoard(2, "Team Board Number 2");
    }

    /**
     * Helper for creating a new default Board for testing
     *
     * @throws FileNotFoundException when files not found
     */
    private void addSetupBoard(
            long id,
            String title
    ) {
        Board board = new Board();
        board.id = id;
        board.title = title;

        this.boardController.addBoard(board);
    }

    @DisplayName("Creates a new user")
    @Test
    public void correctlyCreatesUser() {
        User user = new User();
        user.id = 1L;
        user.username = "testUser";

        this.userController.addUser(user);

        assertEquals(user, this.dummyUserRepository.users.get(0));
    }

    @DisplayName("Correcly gets user by username")
    @Test
    public void correctlyGetsUserByUsername() {
        User user = new User();
        user.id = 1L;
        user.username = "testUser";

        this.userController.addUser(user);

        assertEquals(user, this.userController.getUserByUsername("testUser"));
    }

    @DisplayName("Correctly joins user to board")
    @Test
    public void correctlyJoinsOneBoard() {
        User user = new User();
        user.id = 1L;
        user.username = "testUser";

        this.userController.addUser(user);

        Board board = new Board();
        board.id = 1L;
        board.title = "testBoard";

        this.boardController.addBoard(board);

        this.userController.joinBoard(user, board);

        assertEquals(board, this.userController.getJoinedBoardsByUsername("testUser").get(0));
    }

    @DisplayName("Correctly joins user to multiple boards")
    @Test
    public void correctlyJoinsMultipleBoards() {
        User user = new User();
        user.id = 1L;
        user.username = "testUser";

        this.userController.addUser(user);

        Board board1 = new Board();
        board1.id = 1L;
        board1.title = "testBoard1";

        Board board2 = new Board();
        board2.id = 2L;
        board2.title = "testBoard2";

        this.boardController.addBoard(board1);
        this.boardController.addBoard(board2);

        this.userController.joinBoard(user, board1);
        this.userController.joinBoard(user, board2);

        assertEquals(board1, this.userController.getJoinedBoardsByUsername("testUser").get(0));
        assertEquals(board2, this.userController.getJoinedBoardsByUsername("testUser").get(1));
    }


}
