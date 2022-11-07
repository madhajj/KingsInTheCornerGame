package com.sep.kitc.controller;

import com.sep.kitc.*;
import com.sep.kitc.common.CardHandler;
import com.sep.kitc.common.ChatService;
import com.sep.kitc.common.GameRoomService;
import com.sep.kitc.common.exception.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameRoomController implements Initializable {

    private static Logger LOGGER = Logger.getLogger(Application.class.getName());
    private ChatService chatService = RegistryHandler.getChatService();
    public CardHandler cardHandler = RegistryHandler.getCardHandler();
    private GameRoomService gameRoomService = RegistryHandler.getGameRoomService();
    private String gameRoomName;
    private String userID;
    private String cardNameSelected;
    private String stallNameSelected;
    @FXML
    private Button readyButton;
    @FXML
    private ImageView northStallImageView;
    @FXML
    private ImageView northeastKingsStallImageView;
    @FXML
    private ImageView eastStallImageView;
    @FXML
    private ImageView southeastKingsStallImageView;
    @FXML
    private ImageView southStallImageView;
    @FXML
    private ImageView southwestKingsStallImageView;
    @FXML
    private ImageView westStallImageView;
    @FXML
    private ImageView northwestKingsStallImageView;
    @FXML
    private ImageView deckImageView;
    @FXML
    private ImageView northStallTopCardImageView;
    @FXML
    private ImageView northeastKingsStallTopCardImageView;
    @FXML
    private ImageView eastStallTopCardImageView;
    @FXML
    private ImageView southeastKingsStallTopCardImageView;
    @FXML
    private ImageView southStallTopCardImageView;
    @FXML
    private ImageView southwestKingsStallTopCardImageView;
    @FXML
    private ImageView westStallTopCardImageView;
    @FXML
    private ImageView northwestKingsStallTopCardImageView;
    @FXML
    private ImageView cardInHandImageView1;
    @FXML
    private ImageView cardInHandImageView2;
    @FXML
    private ImageView cardInHandImageView3;
    @FXML
    private ImageView cardInHandImageView4;
    @FXML
    private ImageView cardInHandImageView5;
    @FXML
    private ImageView cardInHandImageView6;
    @FXML
    private ImageView cardInHandImageView7;
    @FXML
    private ImageView cardInHandImageView8;
    @FXML
    private ImageView cardInHandImageView9;
    @FXML
    private ImageView cardInHandImageView10;
    @FXML
    private ImageView cardInHandImageView11;
    @FXML
    private ImageView cardInHandImageView12;
    @FXML
    private ImageView cardInHandImageView13;
    @FXML
    private ImageView cardInHandImageView14;
    @FXML
    private ImageView cardInHandImageView15;
    @FXML
    private Label readyLabel;
    @FXML
    private Label howManyPlayersAreReadyLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label turnLabel69;
    @FXML
    private Label chipsLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label nameOfWinnerLabel;
    @FXML
    private Label outLabel;
    @FXML
    private TextField chatTextField;
    @FXML
    private VBox chatVBox = new VBox();
    @FXML
    private Label botsInGameRoomLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameRoomName = DataHandler.getGameRoomName();
        userID = DataHandler.getUserID();
        Image image = ShowImage.findImage("Back of Card");
        deckImageView.setImage(image);
        startChatThread();
        labelThread();
        displayCardsThread();
        gameThread();
    }

    /**
     * Mithilfe von dieser Methode kann man sich auf bereit stellen
     * Es passt den Label an, wenn ready dann grüner Text und wenn nicht ready dann roter Text
     * Zudem wird der Text vom readyButton geändert.
     */

    public void startTheRound(){
        try {
            cardHandler.setDealer(gameRoomName);
            if(cardHandler.isDealer(userID)){
                cardHandler.resetWinner(gameRoomName);
                cardHandler.startTheBots(gameRoomName);
                cardHandler.distributeCards(gameRoomName, userID);
                cardHandler.distributeChips(gameRoomName);
                cardHandler.startGame(gameRoomName);
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen.");
            throw new RuntimeException(e);
        }
    }

    public void readyButtonOnAction(ActionEvent event){
        try {
            if (!cardHandler.hasRoundStarted(gameRoomName)){
                if (!cardHandler.isPlayerReady(userID)){
                    cardHandler.switchPlayerToReady(userID);
                } else {
                    cardHandler.setPlayerToUnready(userID);
                }
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
            throw new RuntimeException(e);
        }
    }

    public void sendButtonOnAction() {
        String message = chatTextField.getText();
        try {
            if (!chatTextField.getText().isEmpty()){
                chatService.sendMessage(gameRoomName, userID, message);
                chatTextField.clear();
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
            throw new RuntimeException(e);
        }
    }
    public void startChatThread() {
        synchronized (this) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Runnable updater = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ArrayList<String> chatMessages = chatService.getMessages(gameRoomName);
                                chatVBox.getChildren().clear();
                                for (String message : chatMessages) {
                                    HBox hbox = new HBox();
                                    hbox.setAlignment(Pos.CENTER_LEFT);
                                    Text text = new Text(message);
                                    TextFlow textFlow = new TextFlow(text);
                                    hbox.getChildren().add(textFlow);
                                    chatVBox.getChildren().add(hbox);
                                }
                            } catch (RemoteException e) {
                                LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
                                throw new RuntimeException(e);
                            }
                        }
                    };
                    while (true) {
                        try {
                            Thread.sleep(200);
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

    public void labelThread(){
        synchronized (this) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Runnable updater = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                botsInGameRoomLabel.setText("Bots im Raum: " + cardHandler.botsInGameRoom(gameRoomName));
                                howManyPlayersAreReadyLabel.setText(cardHandler.howManyPlayersAreReady(gameRoomName) + "/" + gameRoomService.getGameRoomMapWithMaxPlayers().get(gameRoomName));

                                /*
                                if (cardHandler.hasSomeOneWonGame(gameRoomName)){
                                    SceneManager.changeSceneWithoutEvent("won.fxml", 400, 600, readyButton);
                                }
                                 */

                                if (cardHandler.isPlayerOut(userID, gameRoomName)){
                                    outLabel.setText("Du musst warten bis das Spiel erneut anfängt");
                                } else {
                                    outLabel.setText("");
                                }

                                if (cardHandler.whoWonRound(gameRoomName) != null){
                                    nameOfWinnerLabel.setText(cardHandler.whoWonRound(gameRoomName) + " hat die Runde gewonnen!");
                                } else {
                                    nameOfWinnerLabel.setText("");
                                }

                                if (cardHandler.isPlayerReady(userID)){
                                    readyLabel.setTextFill(Color.color(0, 1, 0));
                                    readyLabel.setText("Bereit");
                                    readyButton.setText("Nicht Bereit");
                                } else {
                                    readyLabel.setTextFill(Color.color(1, 0, 0));
                                    readyLabel.setText("Nicht Bereit");
                                    readyButton.setText("Bereit");
                                }

                                if (cardHandler.isItThePlayersTurn(userID)){
                                    turnLabel69.setTextFill(Color.color(0, 1, 0));
                                    turnLabel69.setText("Dein Zug!");
                                } else {
                                    turnLabel69.setTextFill(Color.color(1, 0, 0));
                                    turnLabel69.setText("Nicht dein Zug!");
                                }

                                chipsLabel.setText("Chips: " + cardHandler.getThePlayersChips(userID));
                                scoreLabel.setText("Score: " + cardHandler.getThePlayersScore(userID));
                            } catch (RemoteException e) {
                                LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
                                throw new RuntimeException(e);
                            }
                        }
                    };
                    while (true) {
                        try {
                            Thread.sleep(210);
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

    public void displayCardsThread(){
        synchronized (this) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Runnable updater = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ArrayList<String> handOfPlayer = cardHandler.getHandOfPlayer(userID);
                                HashMap<String, ArrayList<String>> gameStalls = cardHandler.getTheGameStalls(gameRoomName);
                                updateHandOfPlayer(handOfPlayer);
                                updateGameField(gameStalls);
                            } catch (RemoteException e) {
                                LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
                                throw new RuntimeException(e);
                            }
                        }
                    };
                    while (true) {
                        try {
                            Thread.sleep(220);
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

    /**
     * Diese Methode startet den Spielthread,
     * wenn alle Spieler bereit sind und das Spiel beginnt
     */


    public void gameThread() {
        synchronized (this) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    Runnable updater = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (cardHandler.hasSomeoneWon(gameRoomName) && cardHandler.hasRoundStarted(gameRoomName)){
                                    cardHandler.endRound(gameRoomName);
                                }
                                if (cardHandler.isMaxPlayersReached(gameRoomName)){
                                    if (cardHandler.isEveryPlayerReady(gameRoomName)){
                                        if (!cardHandler.hasRoundStarted(gameRoomName)){
                                            startTheRound();
                                        } else {
                                            infoLabel.setText("");
                                        }
                                    } else {
                                        infoLabel.setText("Nicht alle Spieler \n sind bereit!");
                                    }
                                }
                            } catch (RemoteException e) {
                                LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
                                throw new RuntimeException(e);
                            } catch (NotAllPlayersJoinedException e) {
                                infoLabel.setText("Noch nicht alle Spieler sind beigetreten!");
                            }
                        }
                    };

                    while (true) {
                        try {
                            Thread.sleep(300);
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

    /**
     * Diese Methode aktualisiert die Hand des Spielers mit den korrekten Kartenbildern.
     * @param handOfPlayer
     */
    public synchronized void updateHandOfPlayer(ArrayList<String> handOfPlayer){
        int i = 1;
        cardInHandImageView1.setImage(null);
        cardInHandImageView2.setImage(null);
        cardInHandImageView3.setImage(null);
        cardInHandImageView4.setImage(null);
        cardInHandImageView5.setImage(null);
        cardInHandImageView6.setImage(null);
        cardInHandImageView7.setImage(null);
        cardInHandImageView8.setImage(null);
        cardInHandImageView9.setImage(null);
        cardInHandImageView10.setImage(null);
        cardInHandImageView11.setImage(null);
        cardInHandImageView12.setImage(null);
        cardInHandImageView13.setImage(null);
        cardInHandImageView14.setImage(null);
        cardInHandImageView15.setImage(null);
        if (!handOfPlayer.isEmpty()){
            for (String card : handOfPlayer) {
                Image image = ShowImage.findImage(card);
                if (i == 1){
                    cardInHandImageView1.setImage(image);
                }
                if (i == 2){
                    cardInHandImageView2.setImage(image);
                }
                if (i == 3){
                    cardInHandImageView3.setImage(image);
                }
                if (i == 4){
                    cardInHandImageView4.setImage(image);
                }
                if (i == 5){
                    cardInHandImageView5.setImage(image);
                }
                if (i == 6){
                    cardInHandImageView6.setImage(image);
                }
                if (i == 7){
                    cardInHandImageView7.setImage(image);
                }
                if (i == 8){
                    cardInHandImageView8.setImage(image);
                }
                if (i == 9){
                    cardInHandImageView9.setImage(image);
                }
                if (i == 10){
                    cardInHandImageView10.setImage(image);
                }
                if (i == 11){
                    cardInHandImageView11.setImage(image);
                }
                if (i == 12){
                    cardInHandImageView12.setImage(image);
                }
                if (i == 13){
                    cardInHandImageView13.setImage(image);
                }
                if (i == 14){
                    cardInHandImageView14.setImage(image);
                }
                if (i == 15){
                    cardInHandImageView15.setImage(image);
                }
                i++;
            }
        }
    }

    public void updateHelper(ArrayList<String> stallArray, ImageView bottom, ImageView top){
        if (stallArray.get(0).equals(stallArray.get(1))){
            Image imageNorthStall = ShowImage.findImage(stallArray.get(1));
            bottom.setImage(imageNorthStall);
        } else {
            Image image = ShowImage.findImage(stallArray.get(1));
            bottom.setImage(image);
            Image imageTopCard = ShowImage.findImage(stallArray.get(0));
            top.setImage(imageTopCard);
        }
    }

    public void updateGameField(HashMap<String, ArrayList<String>> gameStalls){
        gameStalls.forEach((name, stallArray) -> {
            try {
                if (cardHandler.isDeckOfGameRoomEmpty(gameRoomName)){
                    deckImageView.setImage(null);
                } else {
                    Image image = ShowImage.findImage("Back of Card");
                    deckImageView.setImage(image);
                }
                if (name.equals("northStall") && !stallArray.isEmpty()){
                    updateHelper(stallArray, northStallImageView, northStallTopCardImageView);
                }
                if (name.equals("northeastKingsStall") && !stallArray.isEmpty()){
                    updateHelper(stallArray, northeastKingsStallImageView, northeastKingsStallTopCardImageView);
                }
                if (name.equals("eastStall") && !stallArray.isEmpty()){
                    updateHelper(stallArray, eastStallImageView, eastStallTopCardImageView);
                }
                if (name.equals("southeastKingsStall") && !stallArray.isEmpty()){
                    updateHelper(stallArray, southeastKingsStallImageView, southeastKingsStallTopCardImageView);
                }
                if (name.equals("southStall") && !stallArray.isEmpty()){
                    updateHelper(stallArray, southStallImageView, southStallTopCardImageView);
                }
                if (name.equals("southwestKingsStall") && !stallArray.isEmpty()){
                    updateHelper(stallArray, southwestKingsStallImageView, southwestKingsStallTopCardImageView);
                }
                if (name.equals("westStall") && !stallArray.isEmpty()){
                    updateHelper(stallArray, westStallImageView, westStallTopCardImageView);
                }
                if (name.equals("northwestKingsStall") && !stallArray.isEmpty()){
                    updateHelper(stallArray, northwestKingsStallImageView, northwestKingsStallTopCardImageView);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Das Neuladen von den Karten hat nicht funktioniert!");
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Diese Methode beendet den Zug des Spielers.
     */
    public void endTurnButtonOnAction(){
        try {
            cardHandler.endTurn(userID, gameRoomName);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
            throw new RuntimeException(e);
        } catch (NoMoreCardsInDeckException e) {
            LOGGER.log(Level.SEVERE, "Das Deck im Spielraum ist leer");
        }
    }


    /**
     * Entfernt die Randmarkierung, wenn eine Karte im Stall deselektiert wird.
     */
    public void removeEffectOnHand(){
        cardInHandImageView1.setEffect(null);
        cardInHandImageView2.setEffect(null);
        cardInHandImageView3.setEffect(null);
        cardInHandImageView4.setEffect(null);
        cardInHandImageView5.setEffect(null);
        cardInHandImageView6.setEffect(null);
        cardInHandImageView7.setEffect(null);
        cardInHandImageView8.setEffect(null);
        cardInHandImageView9.setEffect(null);
        cardInHandImageView10.setEffect(null);
        cardInHandImageView11.setEffect(null);
        cardInHandImageView12.setEffect(null);
        cardInHandImageView13.setEffect(null);
        cardInHandImageView14.setEffect(null);
        cardInHandImageView15.setEffect(null);
    }

    /**
     * entfernt die Randmarkierung, wenn eine Karte auf der Hand deselektiert wird
     */
    public void removeEffectOnStall(){
        northeastKingsStallImageView.setEffect(null);
        northeastKingsStallTopCardImageView.setEffect(null);
        southeastKingsStallImageView.setEffect(null);
        southeastKingsStallTopCardImageView.setEffect(null);
        southwestKingsStallImageView.setEffect(null);
        southwestKingsStallTopCardImageView.setEffect(null);
        northwestKingsStallImageView.setEffect(null);
        northwestKingsStallTopCardImageView.setEffect(null);
        northStallImageView.setEffect(null);
        northStallTopCardImageView.setEffect(null);
        eastStallImageView.setEffect(null);
        eastStallTopCardImageView.setEffect(null);
        southStallImageView.setEffect(null);
        southStallTopCardImageView.setEffect(null);
        westStallImageView.setEffect(null);
        westStallTopCardImageView.setEffect(null);
    }

    public void setEffect(MouseEvent event, Color color){
        ImageView image = (ImageView) event.getSource();
        removeEffectOnStall();
        removeEffectOnHand();
        int depth = 70;
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(color);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        image.setEffect(borderGlow);
    }

    public void play(MouseEvent event, String stallName, boolean isKingsStall){
        try {
            if (stallNameSelected == null && cardNameSelected != null){
                cardHandler.playThisCard(userID, gameRoomName, stallName, cardNameSelected);
                cardNameSelected = null;
                removeEffectOnHand();
            } else if (stallNameSelected == null && !isKingsStall){
                setEffect(event, Color.BLUE);
                stallNameSelected = stallName;
            } else if (stallNameSelected != null){
                cardHandler.mergeTheseStalls(userID, gameRoomName, stallNameSelected, stallName);
                stallNameSelected = null;
                removeEffectOnStall();
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
            throw new RuntimeException(e);
        } catch (NotThePlayersTurnException e) {
            LOGGER.log(Level.SEVERE, "Es ist nicht dein Zug!");
            infoLabel.setText("Es ist nicht dein Zug!");
        } catch (IllegalMoveException e) {
            LOGGER.log(Level.SEVERE, "Ungültiger Zug!");
            infoLabel.setText("Ungültiger Zug!");
            stallNameSelected = null;
            removeEffectOnStall();
        }
    }


    public void selectNorthStall(MouseEvent event){
        play(event, "northStall", false);
    }

    public void selectNortheastKingsStall(MouseEvent event){
        play(event, "northeastKingsStall", true);
    }

    public void selectEastStall(MouseEvent event){
        play(event, "eastStall", false);
    }

    public void selectSoutheastKingsStall(MouseEvent event){
        play(event, "southeastKingsStall", true);
    }

    public void selectSouthStall(MouseEvent event){
        play(event, "southStall", false);
    }

    public void selectSouthwestKingsStall(MouseEvent event){
        play(event, "southwestKingsStall", true);
    }

    public void selectWestStall(MouseEvent event){
        play(event, "westStall", false);
    }

    public void selectNorthwestKingsStall(MouseEvent event){
        play(event, "northwestKingsStall", true);
    }

    /**
     * fügt eine Randmarkierung hinzu, wenn eine Karte auf der Hand ausgewählt wird.
     * @param event
     */
    public void selectedCardInHand(MouseEvent event) {
        ImageView image = (ImageView) event.getSource();
        cardNameSelected = ShowImage.readCardName(image.getImage());
        setEffect(event, Color.RED);
    }

    /**
     * fügt dem Raum einen einfachen Bot hinzu.
     */
    public void addEasyBotButtonOnAction(){
        try {
            if (!cardHandler.hasRoundStarted(gameRoomName)){
                cardHandler.addEasyBot(gameRoomName);
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
            throw new RuntimeException(e);
        }
    }

    /**
     * Fügt dem Raum einen mittlerer Bot hinzu.
     */
    public void addMediumBotButtonOnAction(){
        try {
            if (!cardHandler.hasRoundStarted(gameRoomName)){
                cardHandler.addMediumBot(gameRoomName);
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
            throw new RuntimeException(e);
        }
    }
    public void removeBotButtonOnAction(){
        try {
            if (!cardHandler.hasRoundStarted(gameRoomName)){
                cardHandler.removeBot(gameRoomName);
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
            throw new RuntimeException(e);
        }
    }

    public void backButtonOnAction(ActionEvent event) {
        try {
            cardHandler.removePlayerOutOfGameRoom(gameRoomName, userID);
            cardHandler.resetPlayer(userID);
            SceneManager.changeScene("gameRoomManagement.fxml", 400, 744, event);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Verbindung zum Server wurde unterbrochen");
            throw new RuntimeException(e);
        }
    }
}
