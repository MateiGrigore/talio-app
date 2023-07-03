import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AppController;
import server.BoardController;
import server.RandomPassword;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BoardDatabseControllerTest {

    private BoardController boardController;
    private DummyBoardRepository dummyBoardRepository;

    @BeforeEach
    public void setup() {
        //Hard-coded a new board for the single-board application
        this.dummyBoardRepository = new DummyBoardRepository();
        this.boardController = new BoardController(dummyBoardRepository);
    }

    private void addFirstSetupBoard() throws FileNotFoundException {
        Board board = new Board();
        board.id = 1;
        board.title = "first board";

        // invoke `add`
        boardController.addBoard(board);
    }

    private void addSecondSetupBoard() throws FileNotFoundException {
        Board board = new Board();
        board.id = 2;
        board.title = "Team Board Number 2";

        // invoke `add`
        boardController.addBoard(board);
    }

    @DisplayName("Board Creation")
    @Test
    public void correctlyCreatesBoard() throws IOException {
        addFirstSetupBoard();

        // create model object
        Board model = new Board();
        model.id = 1;
        model.title = "first board";

        // check if exists on dummy repo
        assertEquals(model, dummyBoardRepository.boards.get(0));
    }

    @DisplayName("Get All Boards")
    @Test
    public void correctlyListsAllBoardsAdded() throws FileNotFoundException {
        addFirstSetupBoard();
        addSecondSetupBoard();

        // create model objects
        Board model1 = new Board();
        model1.id = 1;
        model1.title = "first board";

        Board model2 = new Board();
        model2.id = 2;
        model2.title = "Team Board Number 2";

        // create model list
        List<Board> model = new ArrayList<>();
        model.add(model1);
        model.add(model2);

        // get all boards
        List<Board> boardsExtracted = boardController.getAllBoards();

        // assert
        assertEquals(model, boardsExtracted);
    }

    @DisplayName("Get Board by ID")
    @Test
    public void correctlyListsFindBoardByID() throws FileNotFoundException {
        addFirstSetupBoard();
        addSecondSetupBoard();

        // create model objects
        Board model1 = new Board();
        model1.id = 1;
        model1.title = "first board";

        Board model2 = new Board();
        model2.id = 2;
        model2.title = "Team Board Number 2";

        // create model list
        List<Board> model = new ArrayList<>();
        model.add(model1);
        model.add(model2);

        // get all boards
        Board boardExtracted = boardController.getBoardByID(2);

        // assert
        assertEquals(model2, boardExtracted);
    }

    @DisplayName("Edit board")
    @Test
    public void correctlyEditsBoard() throws FileNotFoundException {
        addFirstSetupBoard();

        Board expected = new Board();
        expected.id = 1;
        expected.title = "edited";

        // get the update board
        boardController.saveBoardOverwrite(expected);
        Board newBoard = boardController.getBoardByID(1);

        assertEquals(expected, newBoard);
    }

    @DisplayName("Delete board")
    @Test
    public void correctlyDeletesBoard() throws FileNotFoundException {
        addFirstSetupBoard();

        assertNotNull(boardController.getBoardByID(1L));

        // delete the board
        boardController.deleteBoard(boardController.getBoardByID(1L));
        Board deletedBoard = boardController.getBoardByID(1L);

        // assert
        assertNull(deletedBoard);
    }

    @DisplayName("Delete all boards")
    @Test
    public void correctlyDeletesAllBoards() throws FileNotFoundException {
        addFirstSetupBoard();
        addSecondSetupBoard();

        assertNotNull(boardController.getBoardByID(1L));
        assertNotNull(boardController.getBoardByID(2L));

        boardController.deleteAllBoards(boardController.getAllBoards());

        assertNull(boardController.getBoardByID(1L));
        assertNull(boardController.getBoardByID(2L));
    }
}
