package com.sep.kitc.controller;

import com.sep.kitc.Application;
import com.sep.kitc.DataHandler;
import com.sep.kitc.RegistryHandler;
import com.sep.kitc.SceneManager;
import com.sep.kitc.common.LoginService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyController implements Initializable {

    private static Logger LOGGER = Logger.getLogger(Application.class.getName());

    private LoginService loginService = RegistryHandler.getLogInService();
    @FXML
    private VBox leaderBoardVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startRankThread();
    }
    public void logoutButtonOnAction(ActionEvent event){
        try {
            loginService.logout(DataHandler.getUserID());
            SceneManager.changeScene("login.fxml", 400, 600, event);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Die Verbindung zum Server wurde unterbrochen");
            throw new RuntimeException(e);
        }
    }
    public void accountManagementButtonOnAction(ActionEvent event){
        SceneManager.changeScene("accountManagement.fxml", 400, 600, event);
    }
    public void gameRoomManagementButtonOnAction(ActionEvent event){
        SceneManager.changeScene("gameRoomManagement.fxml", 400, 744, event);
    }

    /**
     * Startet einen Thread, der die Ränge der Spieler ständig aktualisiert und sie der Liste hinzufügt.
     */
    public void startRankThread() {
        synchronized (this) {
            Thread t1 = new Thread(() -> {
                Runnable updater = () -> {
                    try {
                        HashMap<String, Integer> players = loginService.getRankList();
                        leaderBoardVBox.getChildren().clear();
                        int i = 1;
                        for (Map.Entry<String, Integer> entry : players.entrySet()) {
                            String name = entry.getKey();
                            Integer score = entry.getValue();
                            if (name != null) {
                                HBox hbox = new HBox();
                                hbox.setAlignment(Pos.CENTER_LEFT);
                                Text text = new Text(i + ". Benutzername: " + name + ", Punktzahl: " + score);
                                TextFlow textFlow = new TextFlow(text);
                                hbox.getChildren().add(textFlow);
                                leaderBoardVBox.getChildren().add(hbox);
                                i++;
                            }
                        }
                    } catch (RemoteException e) {
                        LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
                        throw new RuntimeException(e);
                    }
                };
                while (true) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Platform.runLater(updater);
                }
            });
            t1.start();
        }
    }
}
