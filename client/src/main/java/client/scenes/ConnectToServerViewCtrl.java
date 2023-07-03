package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectToServerViewCtrl {
    private final TalioMainCtrl mainCtrl;
    private String serverPassword;

    @FXML
    private TextField serverAddress;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Label errorMessage;

    private ServerUtils serverUtils;


    /**
     * TODO Remove Default connect for Admin
     * @param mainCtrl    The main controller class
     * @param serverUtils The Server Utils class
     */
    @Inject
    public ConnectToServerViewCtrl(TalioMainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    /**
     * Connects to the server and changes the scene to the Board View
     */
    public void connect() throws InterruptedException {
        serverUtils.setServer(serverAddress.getText());
        serverUtils.connectToWebsocket();
        serverUtils.createUser(username.getText());
        mainCtrl.loadUpdatedBoards();
        mainCtrl.showBoardMenuView();
    }

    /**
     * Uses the default server address
     */
    public void useDefaultAddress() throws InterruptedException {
        serverUtils.connectToWebsocket();
        serverUtils.createUser(username.getText());
        mainCtrl.loadUpdatedBoards();
        mainCtrl.showBoardMenuView();
    }

    /**
     * Connects a user to the admin menu
     * @throws InterruptedException
     */
    public void connectAsAdmin() throws InterruptedException {
        if(password.getText().equals(serverPassword) && username.getText().equals("admin")){
            errorMessage.setVisible(false);
            serverUtils.connectToWebsocket();
            serverUtils.createUser(username.getText());
            mainCtrl.initializeAdminMenuView();
            mainCtrl.showAdminMenuView();
        } else{
            errorMessage.setText("Wrong Password");
            errorMessage.setStyle("-fx-color-label-visible: red");
            errorMessage.setVisible(true);
        }

    }

    /**
     * Getter for the server address
     *
     * @return Server address
     */
    public TextField getServerAddress() {
        return serverAddress;
    }

    /**
     * Sets the serverPassword field to the actual server password
     */
    public void setServerPassword(){
        if(serverPassword == null){
            serverPassword = serverUtils.getServerPassword();
        }
    }
}
