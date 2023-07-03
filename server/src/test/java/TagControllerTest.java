import commons.Board;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AppController;
import server.RandomPassword;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TagControllerTest {
    private DummyBoardRepository dummyBoardRepository;
    private DummyListRepository dummyListRepository;
    private DummyCardRepository dummyCardRepository;
    private DummyTaskRepository taskRepository;

    private DummyTagRepository tagRepository;
    private AppController controller;

    private RandomPassword randomPassword;

    /**
     * Sets up a clean testing environment with dummy repositories
     * before each test
     */
    @BeforeEach
    private void setupTestingEnv() throws FileNotFoundException {
        // setup dummy repositories
        this.dummyBoardRepository = new DummyBoardRepository();
        this.dummyListRepository = new DummyListRepository();
        this.dummyCardRepository = new DummyCardRepository();
        this.taskRepository = new DummyTaskRepository();
        this.tagRepository = new DummyTagRepository();

        // setup app controller
        this.controller = new AppController(
                this.dummyBoardRepository,
                this.dummyListRepository,
                this.dummyCardRepository,
                this.taskRepository,
                null,
                this.tagRepository,
                randomPassword
        );
    }

    /**
     * Helper for creating a new default Board for testing
     *
     * @return
     * @throws FileNotFoundException when files not found
     */
    private Tag addSetupTag(
            Long id,
            String title,
            int r,
            int g,
            int b

    ) {
        Tag tag = new Tag();
        tag.id = id;
        tag.name = title;
        tag.r = r;
        tag.g = g;
        tag.b = b;

        return tag;
    }

    /**
     * @param l        id
     * @param newBoard title
     * @return Board
     */
    private Board addSetupBoard(long l, String newBoard) {
        Board board = new Board();
        board.id = l;
        board.title = newBoard;
        return board;
    }


    /**
     * Tests that a tag can be created
     */
    @Test
    public void testCreateTag() {
        Tag tag = addSetupTag(1L, "new tag", 255, 0, 0);
        assertNotNull(tag);
        assertEquals("new tag", tag.name);
        assertEquals(255, tag.r);
        assertEquals(0, tag.g);
        assertEquals(0, tag.b);
    }

    /**
     * Tests that a tag can be added to a board via the controller
     */
    @Test
    public void testAddTagToBoard() {
        Tag tag = addSetupTag(1L, "new tag", 255, 0, 0);
        Board board = addSetupBoard(1L, "new board");
        board.tags.add(tag);
        assertNotNull(board);
        assertEquals("new board", board.title);
        assertEquals(1, board.tags.size());
    }

    /**
     * Tests adding a tag to a board via the controller
     */
    @Test
    public void testAddTagToBoardViaController() {
        Tag tag = addSetupTag(1L, "new tag", 255, 0, 0);
        Board board = addSetupBoard(1L, "new board");

        board = controller.addBoard(board);

        controller.addTag(tag, String.valueOf(board.id));

        assertNotNull(board);
        assertEquals("new board", board.title);
        assertEquals(1, board.tags.size());
        assertEquals(tag, controller.getTagsForBoard(board.id).get(0));
    }

    /**
     * Tests removing a tag from a board via the controller
     */
    @Test
    public void testRemoveAddedTagFromBoard() {
        Tag tag = addSetupTag(1L, "new tag", 255, 0, 0);
        Board board = addSetupBoard(1L, "new board");

        board = controller.addBoard(board);

        controller.addTag(tag, String.valueOf(board.id));

        assertNotNull(board);
        assertEquals("new board", board.title);
        assertEquals(1, board.tags.size());
        assertEquals(tag, controller.getTagsForBoard(board.id).get(0));

        controller.removeTag(tag, String.valueOf(board.id));

        assertEquals(0, board.tags.size());
    }


}
