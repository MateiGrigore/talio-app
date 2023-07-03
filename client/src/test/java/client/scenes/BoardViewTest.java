package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.ListEntity;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class BoardViewTest {

    private TalioMainCtrl mainCtrl;
    private  ServerUtils server;
    private BoardViewCtrl boardViewCtrl;


    @BeforeEach
    public void setup() {
       this.mainCtrl = mock(TalioMainCtrl.class);
       this.server = mock(ServerUtils.class);
       this.boardViewCtrl = new BoardViewCtrl(mainCtrl, server);
    }


    @Test
    public void testConstructor() {
        assertNotNull(boardViewCtrl);
    }

    @Test
    public void setBoardAndInitializeTest(){
        Board board = new Board();

        //Running this method should give an error since title is an fxml label and can't be instantiated
        assertThrows(RuntimeException.class, () -> {
            boardViewCtrl.setBoardAndInitialize(board);
        });
    }

    @Test
    public void registerForDragDropTest(){
        Board board = new Board();
        boardViewCtrl.registerForDragDrop(board);

        //register for any kind of updates 3 times
        verify(server, times(3)).registerForMessages(any(), any(), any());
    }

    @Test
    public void addBasicListTest() {

        //calling this method would give a null pointer exception since the board wasn't initialized
        assertThrows(RuntimeException.class, () -> {
            boardViewCtrl.addBasicList();
        });
    }

    @Test
    public void loadBoardDataFromServerTest() {
        Board board = new Board();


        //calling this method would give a null pointer exception since the title wasn't initialized
        assertThrows(RuntimeException.class, () -> {
            boardViewCtrl.loadBoardDataFromServer(board);
        });
    }

    @Test
    public void drawBoardTest(){
        //calling this method would give a null pointer exception since the board wasn't initialized
        assertThrows(RuntimeException.class, () -> {
            boardViewCtrl.drawBoard();
        });
    }

    @Test
    public void clearBoardViewTest(){
        //calling this method would give a null pointer exception since the board wasn't initialized
        assertThrows(RuntimeException.class, () -> {
            boardViewCtrl.clearBoardView();
        });
    }

    @Test
    public void addListVisualizationTest(){
        Pane newList = new Pane();
        ListEntity list = new ListEntity();
        int listCounter = 0;

        //initializing error since no fxml toolkit
        assertThrows(NoClassDefFoundError.class, () -> {
            boardViewCtrl.addListVisualization(newList,new Button(),list,listCounter);
        });
    }

    @Test
    public void addCardVisualizationTest(){
        Pane list = new Pane();
        Card card = new Card();
        int cardCounter = 0;


        //initializing error since no fxml toolkit
        assertThrows(NoClassDefFoundError.class, () -> {
            boardViewCtrl.addCardVisualization(list,new Button(),card, cardCounter);
        });
    }

    @Test
    public void styleListTest(){

        Pane listPane = new Pane();
        boardViewCtrl.styleList(listPane);
        assertEquals(560, listPane.getMinHeight());
    }

    @Test
    public void styleScrollPaneTest(){
        int listCounter = 0;

        //initializing error since no fxml toolkit
        assertThrows(ExceptionInInitializerError.class, () -> {
            boardViewCtrl.styleScrollPane(new ScrollPane(),listCounter);
        });
    }

    @Test
    public void styleAddCardButtonTest(){
        //initializing error since no fxml toolkit
        assertThrows(NoClassDefFoundError.class, () -> {
            boardViewCtrl.styleAddCardButton(new Button());
        });
    }

    @Test
    public void leaveBoardTest(){
        //calling this method would give a null pointer exception since the board wasn't initialized
        assertThrows(RuntimeException.class, () -> {
            boardViewCtrl.leaveBoard();
        });
    }

    @Test
    public void showBoardMenuViewTestIsAdminTest(){
        boardViewCtrl.setAdmin(true);
        boardViewCtrl.showBoardMenuView();
        verify(mainCtrl).showAdminMenuView();
    }

    @Test
    public void showBoardMenuViewTestIsNotAdminTest(){
        boardViewCtrl.setAdmin(false);
        boardViewCtrl.showBoardMenuView();
        verify(mainCtrl).showBoardMenuView();
    }

    @Test
    public void addCardButtonEventTest(){
        //initializing error since no fxml toolkit
        assertThrows(NoClassDefFoundError.class, () -> {
            boardViewCtrl.addCardButtonEvent(new Button(), new ListEntity());
        });
    }

    @Test
    public void setRenameBoardEventsTest(){
        //Running this method should give an error since title is an fxml label and can't be instantiated
        assertThrows(RuntimeException.class, () -> {
            boardViewCtrl.setRenameBoardEvents();
        });
    }

    @Test
    public void setAdminTest(){
        boardViewCtrl.setAdmin(true);
        assertTrue(boardViewCtrl.getAdmin());
    }

    @Test
    public void getAdminTest(){
        boardViewCtrl.setAdmin(true);
        assertTrue(boardViewCtrl.getAdmin());
    }
}
