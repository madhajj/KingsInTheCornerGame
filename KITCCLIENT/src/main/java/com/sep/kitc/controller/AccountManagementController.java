package com.sep.kitc.controller;

import com.sep.kitc.Application;
import com.sep.kitc.DataHandler;
import com.sep.kitc.RegistryHandler;
import com.sep.kitc.SceneManager;
import com.sep.kitc.common.CardHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountManagementController implements Initializable {
    private static CardHandler cardHandler = RegistryHandler.getCardHandler();
    private static Logger LOGGER = Logger.getLogger(Application.class.getName());
    @FXML
    private Label gamesWonLabel;
    @FXML
    private Label userNameLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String userName = DataHandler.getUsername();
        String userID = DataHandler.getUserID();
        try {
            int score = cardHandler.getThePlayersTotalScore(userID);
            userNameLabel.setText(userName);
            gamesWonLabel.setText(String.valueOf(score));
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Die Verbindung zum Server wurde unterbrochen");
            e.printStackTrace();
        }
    }
    public void deleteAccountButtonOnAction(ActionEvent event){
        SceneManager.changeScene("deleteAccount.fxml", 400, 600, event);
    }
    public void lobbyButtonOnAction(ActionEvent event){
        SceneManager.changeScene("lobby.fxml", 400, 600, event);
    }


}
