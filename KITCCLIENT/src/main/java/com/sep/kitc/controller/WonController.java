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

public class WonController implements Initializable {

    private static Logger LOGGER = Logger.getLogger(Application.class.getName());
    private CardHandler cardHandler = RegistryHandler.getCardHandler();
    @FXML
    private Label wonLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            wonLabel.setText(cardHandler.whoWonGame(DataHandler.getGameRoomName()) + " hat das Spiel gewonnen.");
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Die Verbindung zum Server wurde unterbrochen");
            throw new RuntimeException(e);
        }
    }

    public void backButtonOnAction(ActionEvent event){
        SceneManager.changeScene("gameRoomManagement.fxml", 400, 744, event);
    }
}
