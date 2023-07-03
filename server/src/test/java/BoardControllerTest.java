import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AppController;
import server.RandomPassword;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardControllerTest {

    private AppController controller;
    private DummyBoardRepository dummyBoardRepository;

    private RandomPassword randomPassword;

    @BeforeEach
    public void setup() {
        //Hard-coded a new board for the single-board application
        this.dummyBoardRepository = new DummyBoardRepository();
        this.controller = new AppController(dummyBoardRepository, null, null, null, null, null, randomPassword);
    }

    private void addFirstSetupBoard() throws FileNotFoundException {
        Board board = new Board();
        board.id = 1;
        board.title = "first board";

        // invoke `add`
        this.controller.addBoard(board);
    }

    private void addSecondSetupBoard() throws FileNotFoundException {
        Board board = new Board();
        board.id = 2;
        board.title = "Team Board Number 2";

        // invoke `add`
        this.controller.addBoard(board);
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
        assertEquals(model, this.dummyBoardRepository.boards.get(0));
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
        List<Board> boardsExtracted = this.controller.getAllBoards();

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
        Board boardExtracted = this.controller.getBoardByID(2);

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
        this.controller.editBoard(expected);
        Board newBoard = this.controller.getBoardByID(1);

        assertEquals(expected, newBoard);
    }

    @DisplayName("Delete board")
    @Test
    public void correctlyDeletesBoard() throws FileNotFoundException {
        addFirstSetupBoard();

        assertNotNull(this.controller.getBoardByID(1L));

        // delete the board
        this.controller.removeBoard(this.controller.getBoardByID(1L));
        Board deletedBoard = this.controller.getBoardByID(1L);

        // assert
        assertNull(deletedBoard);
    }

    @DisplayName("Delete all boards")
    @Test
    public void correctlyDeletesAllBoards() throws FileNotFoundException {
        addFirstSetupBoard();
        addSecondSetupBoard();

        assertNotNull(controller.getBoardByID(1L));
        assertNotNull(controller.getBoardByID(2L));

        controller.deleteAllBoards(controller.getAllBoards());

        assertNull(controller.getBoardByID(1L));
        assertNull(controller.getBoardByID(2L));
    }
}