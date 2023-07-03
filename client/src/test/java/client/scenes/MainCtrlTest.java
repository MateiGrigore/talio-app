/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.MyFXML;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Board;
import commons.Card;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.google.inject.Guice.createInjector;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class MainCtrlTest {

    private static final Injector INJECTOR = createInjector(new MyModule());
    public static final MyFXML FXML = new MyFXML(INJECTOR);

    private TalioMainCtrl talioMainCtrl;

    private Stage primaryStage;

    private ServerUtils serverUtils;


    @Mock
    private BoardViewCtrl boardViewCtrl;

    @Mock
    private ConnectToServerViewCtrl connectToServerViewCtrl;

    @Mock
    private BoardMenuViewCtrl boardMenuViewCtrl;

    @Mock
    private CreateBoardViewCtrl createBoardViewCtrl;

    @Mock
    private EditCardViewCtrl editCardViewCtrl;

    @Mock
    private EditBoardViewCtrl editBoardViewCtrl;

    @Mock
    private AdminMenuViewCtrl adminMenuViewCtrl;

    @Mock
    private BoardTagOverviewCtrl boardTagOverviewCtrl;

    @Mock
    private AddTagViewCtrl addTagViewCtrl;

    @Mock
    private Parent parent;


    @BeforeEach
    public void setUp() throws InterruptedException {
        talioMainCtrl = new TalioMainCtrl();
        serverUtils = mock(ServerUtils.class);
        primaryStage = mock(Stage.class);
        MockitoAnnotations.openMocks(this);
        talioMainCtrl = new TalioMainCtrl();
    }

    @Test
    public void testInitialize() {
        Pair<BoardViewCtrl, Parent> boardViewPair =
                new Pair<>(boardViewCtrl, parent);
        Pair<ConnectToServerViewCtrl, Parent> connectToServerPair =
                new Pair<>(connectToServerViewCtrl, parent);
        Pair<BoardMenuViewCtrl, Parent> boardMenuPair =
                new Pair<>(boardMenuViewCtrl, parent);
        Pair<CreateBoardViewCtrl, Parent> createBoardPair =
                new Pair<>(createBoardViewCtrl, parent);
        Pair<EditCardViewCtrl, Parent> editCardPair =
                new Pair<>(editCardViewCtrl, parent);
        Pair<EditBoardViewCtrl, Parent> editBoardPair =
                new Pair<>(editBoardViewCtrl, parent);
        Pair<AdminMenuViewCtrl, Parent> adminMenuPair =
                new Pair<>(adminMenuViewCtrl, parent);
        Pair<BoardTagOverviewCtrl, Parent> tagOverviewPair =
                new Pair<>(boardTagOverviewCtrl, parent);
        Pair<AddTagViewCtrl, Parent> addTagViewCtrlParentPair=
                new Pair<>(addTagViewCtrl, parent);

        //initializing error since no fxml toolkit
        assertThrows(ExceptionInInitializerError.class, () -> {
            talioMainCtrl.initialize(primaryStage, boardViewPair, connectToServerPair,
                    boardMenuPair, createBoardPair, editCardPair, editBoardPair,
                    adminMenuPair,tagOverviewPair, addTagViewCtrlParentPair, serverUtils);
        });
    }

    @Test
    public void showBoardViewTest(){
        //Null error since views weren't initialized
        assertThrows(RuntimeException.class, () -> {
            talioMainCtrl.showBoardMenuView();
        });
    }
    @Test
    public void loadUpdatedBoardsTest(){
        //Null error since views weren't initialized
        assertThrows(RuntimeException.class, () -> {
            talioMainCtrl.loadUpdatedBoards();
        });
    }@Test
    public void initializeAdminMenuViewTest(){
        //Null error since views weren't initialized
        assertThrows(RuntimeException.class, () -> {
            talioMainCtrl.initializeAdminMenuView();
        });
    }@Test
    public void showCreateBoardView(){
        //Null error since views weren't initialized
        assertThrows(RuntimeException.class, () -> {
            talioMainCtrl.showCreateBoardView(true);
        });
    }@Test
    public void showEditBoardViewTest(){
        //Null error since views weren't initialized
        assertThrows(RuntimeException.class, () -> {
            talioMainCtrl.showEditBoardView(new Board(), true);
        });
    }@Test
    public void closeCardOverviewTest(){
        //Null error since views weren't initialized
        assertThrows(RuntimeException.class, () -> {
            talioMainCtrl.closeCardOverview();
        });
    }
    @Test
    public void showCardOverviewTest(){
        //Null error since views weren't initialized
        assertThrows(RuntimeException.class, () -> {
            talioMainCtrl.showCardOverview(new Card());
        });
    }

}