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
package client;

import client.scenes.*;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    public static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * @param args the command line arguments
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     * @param primaryStage the primary stage for this application,
     *                     onto which the application scene can be set.
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        //Modified the template code for our new Classes
        var boardView = FXML.load(BoardViewCtrl.class, "client", "scenes", "BoardView.fxml");
        var connectToServerView = FXML.load(
                ConnectToServerViewCtrl.class, "client", "scenes", "ConnectToServer.fxml");
        var boardMenuView = FXML.load(
                BoardMenuViewCtrl.class, "client", "scenes", "BoardMenu.fxml");
        var createBoardView = FXML.load(
                CreateBoardViewCtrl.class, "client", "scenes", "CreateBoard.fxml");
        var editCardView = FXML.load(EditCardViewCtrl.class, "client", "scenes", "EditCard.fxml");

        var boardTagOverviewView = FXML.load(BoardTagOverviewCtrl.class, "client", "scenes",
                "BoardTagOverview.fxml");
        var addTagView = FXML.load(AddTagViewCtrl.class, "client", "scenes", "AddTagView.fxml");

        var editBoardView = FXML.load(EditBoardViewCtrl.class,
                "client", "scenes", "EditBoard.fxml");

        var adminMenuView = FXML.load(AdminMenuViewCtrl.class,
                "client", "scenes", "AdminMenu.fxml");

        var talioMainCtrl = INJECTOR.getInstance(TalioMainCtrl.class);

        // FIXME: where does this get initialized? Because now we need to make it static.
        var serverUtils = INJECTOR.getInstance(ServerUtils.class);


        talioMainCtrl.initialize(primaryStage, boardView, connectToServerView, boardMenuView,
                createBoardView, editCardView, editBoardView, adminMenuView, boardTagOverviewView,
                addTagView, serverUtils);

        primaryStage.setOnCloseRequest(event -> {
            editCardView.getKey().stop();
        });
    }
}
