package com.sep.kitc.adapter.storage.entity;

import com.sep.kitc.adapter.storage.service.RegistrationService;
import com.sep.kitc.common.exception.IllegalMoveException;
import com.sep.kitc.common.exception.NoMoreCardsInDeckException;
import com.sep.kitc.common.exception.NotThePlayersTurnException;
import com.sep.kitc.common.exception.UserNotCreatedException;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bot extends Player implements Runnable{

    private static Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());

    private String difficulty;

    private boolean flag;

    private GameRoom gameRoom;

    public Bot(String pUsername, String pPassword) {
        super(pUsername, pPassword);
    }

    public String getDifficulty() {
        return difficulty;
    }

    public GameRoom getGameRoom() {
        return gameRoom;
    }

    public synchronized void setFlag(boolean pFlag){
        flag = pFlag;
    }

    public synchronized boolean getFlag(){
        return flag;
    }

    public void setGameRoom(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Diese Methode wird verwendet, wenn der Bot versucht Karten zu spielen.
     * Es wird versucht alle Karten aus der Hand auf alle Stapel zu spielen.
     * @param gameRoom Spielraum in der sich der Spieler befindet.
     */
    public synchronized void tryPlaying(GameRoom gameRoom){
        for (int i = 0; i < getHand().size(); i++){
            Card card = getHand().get(i);
            for (Map.Entry<String, Stall> entry : gameRoom.getGameStalls().entrySet()) {
                Stall stall = entry.getValue();
                try {
                    playCard(card, stall);
                    break;
                } catch (IllegalMoveException e) {
                    //LOGGER.log(Level.SEVERE, "Diese Karte darf man nicht auf diesen Stall legen");
                } catch (NotThePlayersTurnException e) {
                    //LOGGER.log(Level.SEVERE, "Es ist nicht dem gegebenem Spieler sein Zug");
                }
            }
        }
    }

    /**
     * Diese Methode wird verwendet, wenn ein Bot am Zug ist.
     * Zuerst wird überprüft, ob der Bot am Zug ist.
     * Zunächst werden Karten aus der Hand auf alle Stapel gespielt, wenn möglich.
     * Falls es sich um ein "Easy" Bot handelt ruft er die Methode
     * @param gameRoom Spielraum in der sich der Spieler befindet.
     */
    public synchronized void botPlaysCards(GameRoom gameRoom){
        int startHand = getHand().size();
        if(difficulty.equals("Easy")){
            tryPlaying(gameRoom);
        }else{
            tryPlaying(gameRoom);
            if (startHand != getHand().size()){
                botPlaysCards(gameRoom);
            }
        }
    }

    /**
     * Diese Methode gibt dem Spieler links vom Bot den Zug.
     * Konkret wird die Variable "turn" von dem links liegenden Spieler auf "true" gesetzt.
     */
    public synchronized void giveTurnToPlayer(){
        int i = 0;
        for (String ID : gameRoom.getPlayerSet()) {
            if (ID.equals(getUsername())) {
                try {
                    RegistrationService.getRegisteredPlayer(gameRoom.getPlayerSet().get((i + 1) % gameRoom.getPlayerSet().size())).setPlayersTurn(true);
                } catch (UserNotCreatedException e) {
                    LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
                    throw new RuntimeException(e);
                }
            } else {
                i++;
            }
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                if (isPlayersTurn()){
                    botPlaysCards(gameRoom);
                    endTurnOfPlayer(gameRoom);
                    giveTurnToPlayer();
                }
                Thread.sleep(250);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Thread wurde beim Schlafen gestört!");
            } catch (NoMoreCardsInDeckException e) {
                LOGGER.log(Level.SEVERE, "Es befinden sich keine Karten mehr im Deck");
            }
        }
    }
}
