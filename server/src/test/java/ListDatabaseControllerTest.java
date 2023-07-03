import commons.Board;
import commons.ListEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.AppController;
import server.ListController;
import server.RandomPassword;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListDatabaseControllerTest {

    private ListController listController;
    private DummyBoardRepository dummyBoardRepository;
    private DummyListRepository dummyListRepository;

    private DummyCardRepository dummyCardRepository;

    private RandomPassword randomPassword;
    @BeforeEach
    public void setUp() throws FileNotFoundException {
        this.dummyListRepository = new DummyListRepository();
        this.dummyBoardRepository = new DummyBoardRepository();
        this.dummyCardRepository = new DummyCardRepository();
        this.listController = new ListController(dummyListRepository, dummyCardRepository, dummyBoardRepository);

        addSetupBoard(
                1,
                "first board"
        );
        addSetupList(
                1,
                "first list",
                1
        );
    }

    private void addSetupBoard(
            long id,
            String title
    ) {
        Board board = new Board();
        board.id = id;
        board.title = title;

        this.dummyBoardRepository.save(board);
    }

    private void addSetupList(
            long id,
            String name,
            long boardID
    ) {
        ListEntity list = new ListEntity();
        list.id = id;
        list.name = name;
        list.board = this.dummyBoardRepository.getById(boardID);

        this.listController.addList(list, boardID);
    }

    @DisplayName("List Creation")
    @Test
    public void correctlyCreatesList() {
        // create model object
        ListEntity model = new ListEntity();
        model.id = 1;
        model.name = "first list";
        model.setBoard(dummyBoardRepository.boards.get(0));

        // check if exists on dummy repo
        assertEquals(model, this.dummyListRepository.lists.get(0));
    }

    @DisplayName("Get All Lists")
    @Test
    public void correctlyListsAllListsAdded() throws FileNotFoundException {
        // call the setup

        addSetupList(
                2L, "second list", 1
        );

        // create model objects
        ListEntity model1 = new ListEntity();
        model1.id =1;
        model1.name = "first list";
        model1.setBoard(dummyBoardRepository.boards.get(0));


        ListEntity model2 = new ListEntity();
        model2.id=2;
        model2.name = "second list";
        model2.setBoard(dummyBoardRepository.boards.get(0));

        // create model list
        List<ListEntity> model = new ArrayList<>();
        model.add(model1);
        model.add(model2);

        // get all lists
        List<ListEntity> listsExtracted = this.listController.getAllLists();

        // assert
        assertEquals(model, listsExtracted);
    }

    @DisplayName("Get List by ID")
    @Test
    public void correctlyListsFindListByID() throws FileNotFoundException {
        // call the setup

        addSetupList(
                2,
                "second list",
                1
        );

        // create model objects
        ListEntity model1 = new ListEntity();
        model1.id = 1;
        model1.name = "first list";
        model1.setBoard(dummyBoardRepository.boards.get(0));


        ListEntity model2 = new ListEntity();
        model2.id=2;
        model2.name = "second list";
        model2.setBoard(dummyBoardRepository.boards.get(0));

        // create model list
        List<ListEntity> model = new ArrayList<>();
        model.add(model1);
        model.add(model2);


        // get the list
        ListEntity listExtracted = this.listController.getListByID(2);

        // assert
        assertEquals(model2, listExtracted);
    }

    @DisplayName("Get Lists by boardID")
    @Test
    public void correctlyListsFindListByBoardID() throws FileNotFoundException {
        addSetupList(
                2,
                "second list",
                1
        );

        // create model objects
        ListEntity model1 = new ListEntity();
        model1.id = 1;
        model1.name = "first list";
        model1.setBoard(dummyBoardRepository.boards.get(0));


        ListEntity model2 = new ListEntity();
        model2.id=2;
        model2.name = "second list";
        model2.setBoard(dummyBoardRepository.boards.get(0));

        // create model list
        List<ListEntity> model = new ArrayList<>();
        model.add(model1);
        model.add(model2);


        // get all lists
        List<ListEntity> listsExtracted = this.listController.getListsByBoardId(1L);

        // assert
        assertEquals(model, listsExtracted);
    }

    @DisplayName("Edit List by ID")
    @Test
    public void correctlyEditsListByID() throws FileNotFoundException {

        // create model objects
        ListEntity model = new ListEntity();
        model.id=1;
        model.name = "edited list";
        model.setBoard(dummyBoardRepository.boards.get(0));

        // perform manual deep copy
        ListEntity fetched = this.dummyListRepository.getById(1L);
        ListEntity edited = new ListEntity();
        edited.board = fetched.board;
        edited.id = 1;
        edited.name = "edited list";

        // call edit lists
        this.listController.saveListOverwrite(edited);

        // get list from repo
        ListEntity listExtracted = this.listController.getListByID(1);

        // assert if updated correctly
        assertEquals(model, listExtracted);
    }

    @DisplayName("Delete List by ID")
    @Test
    public void correctlyDeletesListByID() throws FileNotFoundException {

        assertNotNull(this.listController.getListByID(1));

        // delete the list
        ListEntity listRemoved = this.dummyListRepository.getById(1L);
        this.listController.deleteList(listRemoved);
        ListEntity listExtracted = this.listController.getListByID(1);

        // assert
        assertNull(listExtracted);
    }

    @DisplayName("Get nth list")
    @Test
    public void correctlyGetsNthListFromBoard(){
        ListEntity model2 = new ListEntity();
        model2.id=2;
        model2.name = "second list";
        model2.setBoard(dummyBoardRepository.boards.get(0));

        listController.addList(model2, 1L);
        System.out.println(listController.getListsByBoardId(1));

        assertEquals(model2, listController.getNthListFromBoard(1, 1));
    }
}
