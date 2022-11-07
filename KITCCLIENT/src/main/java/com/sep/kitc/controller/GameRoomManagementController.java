package com.sep.kitc.controller;

import com.sep.kitc.Application;
import com.sep.kitc.DataHandler;
import com.sep.kitc.RegistryHandler;
import com.sep.kitc.SceneManager;
import com.sep.kitc.common.CardHandler;
import com.sep.kitc.common.GameRoomService;
import com.sep.kitc.common.exception.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameRoomManagementController{
    private static Logger LOGGER = Logger.getLogger(Application.class.getName());
    GameRoomService gameRoomService = RegistryHandler.getGameRoomService();
    CardHandler cardHandler = RegistryHandler.getCardHandler();
    @FXML
    private TextField createGameRoomNameTextField;
    @FXML
    private TextField maxPlayerTextField;
    @FXML
    private TextField createGameRoomPasswordField;
    @FXML
    private Label createGameRoomManagementWarningLabel;
    @FXML
    private VBox gameRoomVBox;
    @FXML
    private TextField joinGameRoomNameTextFIeld;
    @FXML
    private TextField joinGameRoomPasswordField;
    @FXML
    private Label joinGameRoomWarningLabel;


    public GameRoomManagementController(){
        gameRoomVBox = new VBox();
        startThread();
    }
    public void createGameRoomButtonOnAction() {
        try {
            String roomName = createGameRoomNameTextField.getText();
            int maxPlayers = Integer.parseInt(maxPlayerTextField.getText());
            String roomPassword = createGameRoomPasswordField.getText();
            if (!roomName.isEmpty() && !String.valueOf(maxPlayers).isEmpty() && !roomPassword.isEmpty()){
                if (maxPlayers > 1 && maxPlayers < 7){
                    if (!gameRoomService.nameAlreadyExists(roomName)){
                        gameRoomService.createGameRoom(roomName, roomPassword, maxPlayers);
                        createGameRoomNameTextField.clear();
                        createGameRoomPasswordField.clear();
                        maxPlayerTextField.clear();
                    } else {
                        LOGGER.log(Level.INFO, "Spielraumname ist bereits vergeben");
                        createGameRoomManagementWarningLabel.setText("Spielraumname ist bereits vergeben");
                    }
                } else {
                    LOGGER.log(Level.INFO, "Die Anzahl an maximalen Spielern muss zwischen 2 und 6 liegen");
                    createGameRoomManagementWarningLabel.setText("Die Anzahl an maximalen Spielern muss zwischen 2 und 6 liegen");
                }
            } else {
                LOGGER.log(Level.INFO, "Spielraumnamen und Spielraumpasswort eingeben");
                createGameRoomManagementWarningLabel.setText("Spielraumnamen und Spielraumpasswort eingeben");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.INFO, "Maximale Anzahl an Spielern muss eine Zahl sein");
            createGameRoomManagementWarningLabel.setText("Maximale Anzahl an spielern muss eine Zahl sein");
        } catch (CreateGameRoomException e) {
            LOGGER.log(Level.INFO, "Spielraum mit diesem Spielraumnamen wurde bereits erstellt");
            createGameRoomManagementWarningLabel.setText("Spielraum mit diesem Spielraumnamen wurde bereits erstellt");
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Verbindung mit dem Server wurde unterbrochen");
            throw new RuntimeException(e);
        } catch (RoomAlreadyExistsException e) {
            createGameRoomManagementWarningLabel.setText("Spielraumname ist schon vergeben");
        }
    }
    public void joinGameRoomButtonOnAction(ActionEvent event){
        String gameRoomName = joinGameRoomNameTextFIeld.getText();
        String roomPassword = joinGameRoomPasswordField.getText();
        String userID = DataHandler.getUserID();

        if (!gameRoomName.isEmpty() && !roomPassword.isEmpty()) {
            try {
                DataHandler.setGameRoomName(gameRoomName);
                gameRoomService.joinGameRoom(gameRoomName, roomPassword, userID);
                joinGameRoomNameTextFIeld.clear();
                joinGameRoomPasswordField.clear();
                SceneManager.changeScene("gameRoom.fxml", 700, 1200, event);
            } catch (RemoteException e) {
                LOGGER.log(Level.SEVERE, "Verbindung mit dem Server unterbrochen");
                throw new RuntimeException(e);
            } catch (PlayerHasAlreadyJoinedException e) {
                LOGGER.log(Level.INFO, "Spieler ist bereits einem Spiel beigetreten.");
                joinGameRoomWarningLabel.setText("Spieler ist bereits einem Spiel beigetreten.");
            } catch (GameRoomNotCreatedException e) {
                LOGGER.log(Level.INFO, "Diesen Spielraum gibt es nicht ");
                joinGameRoomWarningLabel.setText("Diesen Spielraum gibt es nicht");
            } catch (MaxPlayersReachedException e) {
                LOGGER.log(Level.INFO, "Maximale anzahl an Spielern wurde bereits erreicht");
                joinGameRoomWarningLabel.setText("Maximale anzahl an Spielern wurde bereits erreicht");
            } catch (PasswordIncorrectException e) {
                LOGGER.log(Level.INFO, "Falsches Passwort eingegeben");
                joinGameRoomWarningLabel.setText("Falsches Passwort eingegeben");
            }
        } else {
            joinGameRoomWarningLabel.setText("Spielraumname und passwort eingeben");
        }
    }
    public void backButtonOnAction(ActionEvent event) {
        SceneManager.changeScene("lobby.fxml", 400, 600, event);
    }

    /**
     * Startet einen Thread, der die erstellten Räume ständig aktualisiert und der Liste hinzufügt.
     */
    public void startThread(){
        synchronized (this){
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Runnable updater = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HashMap<String, Integer> gameRooms = gameRoomService.getGameRoomMap();
                                gameRoomVBox.getChildren().clear();
                                gameRooms.forEach((gameRoomName, playersInGameRoom) -> {
                                    try {
                                        HBox hbox = new HBox();
                                        hbox.setAlignment(Pos.CENTER_LEFT);
                                        Text text = new Text("Spielraumname: " + gameRoomName + ", Anzahl an Spielern: " + cardHandler.getNumberOfPlayersInGameRoom(gameRoomName) + "/" + gameRoomService.getGameRoomMapWithMaxPlayers().get(gameRoomName));
                                        TextFlow textFlow = new TextFlow(text);
                                        hbox.getChildren().add(textFlow);
                                        gameRoomVBox.getChildren().add(hbox);
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            } catch (RemoteException e) {
                                LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
                                throw new RuntimeException(e);
                            }
                        }
                    };
                    while(true){
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Platform.runLater(updater);
                    }
                }
            });
            t1.start();
        }
    }
}
