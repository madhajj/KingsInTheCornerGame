package com.sep.kitc.adapter.storage.entity;

import com.sep.kitc.adapter.storage.service.RegistrationService;
import com.sep.kitc.common.exception.IllegalMoveException;
import com.sep.kitc.common.exception.NoMoreCardsInDeckException;
import com.sep.kitc.common.exception.NotThePlayersTurnException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player implements Serializable {

    private static Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());
    private final String username;
    private final String password;
    private int chips;
    private int score;
    private int totalScore;

    /**
     * Hier werden die Karten, die der Spieler in dem Moment in der Hand hat, gespeichert.
     */
    private ArrayList<Card> hand;
    private boolean playersTurn;
    private boolean ready;

    private boolean bot;
    private boolean out;
    private boolean wonRound;
    private boolean wonGame;
    private boolean dealer;
    private int playedCards;

    /**
     * Um einen Spieler zu erstellen, braucht man einen Benutzernamen und ein Passwort.
     * Beim Erstellen, werden alle erstellten Variablen auf "0", respektive "false" gesetzt.
     * @param pUsername der Benutzername des Spielers.
     * @param pPassword das Passwort des Spielers.
     */

    public Player(String pUsername, String pPassword) {
        username = pUsername;
        password = pPassword;
        wonRound = false;
        wonGame = false;
        playersTurn = false;
        bot = false;
        ready = false;
        dealer = false;
        out = false;
        playedCards = 0;
        score = 0;
        chips = 0;
        totalScore = 0;
        hand = new ArrayList<>();
    }

    /**
     * Eine Methode, um eine bestimmte Anzahl an Chips zu bezahlen.
     * Zum Schluss gibt es die bezahlten Chips zurück.
     * @param payChips die Anzahl an Chips, die man zahlen muss.
     * @return payChips die Anzahl an Chips, die man zahlen muss.
     */
    public int payChips(int payChips) {
        if (payChips <= chips){
            chips -= payChips;
            return payChips;
        } else {
            out = true;
            return 0;
        }
    }

    /**
     * Eine Methode, um zu überprüfen, ob sich ein König in der Hand des Spielers befindet.
     * @return "true" falls sich ein König in der Hand des Spielers befindet, ansonsten "false".
     */
    public boolean hasKing() {
        for (Card card : hand) {
            if (card.isKing()) {
                return true;
            }
        }
        return false;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean pState){
        out = pState;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public int getTotalScore(){
        return totalScore;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Eine Methode, die der Hand des Spielers eine Karte hinzufügt.
     * @param card die Karte die der Hand hinzugefügt werden soll.
     */
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    /**
     * Eine Methode, die die Punktzahl eines Spielers addiert
     * @param pScore die Punktzahl die Addiert werden soll.
     */
    public void addScore(int pScore) {
        score += pScore;
    }

    public void addToTotalScore(int pScore){
        totalScore += pScore;
    }

    public void setScore(int pScore) {
        score = pScore;
    }

    /**
     * Eine Methode, die die Chips hinzufügt.
     * @param chipsToAdd Chips die hinzugefügt werden sollen.
     */
    public void addChips(int chipsToAdd) {
        chips = chipsToAdd;
    }

    public void setChips(int pChips){
        chips = pChips;
    }

    public void setWonRound(boolean state) {
        wonRound = state;
    }

    public void setWonGame(boolean state) {
        wonGame = state;
    }

    public boolean isPlayersTurn() {
        return playersTurn;
    }

    public synchronized void setPlayersTurn(Boolean state) {
        playersTurn = state;
    }

    public boolean hasWonRound() {
        return wonRound;
    }

    public boolean hasWonGame() {
        return wonGame;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady() {
        ready = true;
    }

    public void setUnReady() {
        ready = false;
    }

    public int getPlayedCards() {
        return playedCards;
    }

    /**
     * Setzt die Anzahl der Karten zurück, die ein Spieler gespielt hat.
     */
    public void resetPlayedCards() {
        playedCards = 0;
    }

    /**
     * Diese Methode erlaubt es dem Spieler eine Karte aus seiner Hand auf einen möglichen Stapel zu spielen.
     * Es benötigt eine Karte, sowie einen Stapel worauf die Karte legen will.
     * Zuerst wird überprüft, ob der Spieler am Zug ist.
     * Dann, prüfen wir, ob es sich um einen gültigen Zug handelt.
     * Laut den Regeln von KingsInTheCorner, darf man eine Karte nur spielen, wenn diese einen Wert
     * kleiner hat als die oberste Karte des Stapels und die eine andere Farbe hat.
     * Falls es sich um einen gültigen Zug handelt, dann wird diese Karte dem Stapel hinzugefügt,
     * und aus der Hand vom Spieler entfernt.
     * Ansonsten wirft die Methode eine Exception.
     * @param card die Karte die man spielen will.
     * @param stall der Stapel worauf man es spielen will.
     * @throws IllegalMoveException falls es sich um einen ungültigen Zug handelt.
     * @throws NotThePlayersTurnException falls der Spieler nicht am Zug ist.
     */
    public void playCard(Card card, Stall stall) throws IllegalMoveException, NotThePlayersTurnException {
        if (isPlayersTurn()) {
            if (stall.isPlayable(card)) {
                stall.addCardToStall(card);
                getHand().remove(card);
                playedCards++;
            } else {
                //LOGGER.log(Level.SEVERE, "Diese Karte darf man nicht auf diesen Stall legen");
                throw new IllegalMoveException("Diese Karte darf man nicht auf diesen Stall legen");
            }
        } else {
            //LOGGER.log(Level.SEVERE, "Diese Karte darf man nicht auf diesen Stall legen");
            throw new NotThePlayersTurnException("Es ist nicht dem gegebenem Spieler sein Zug");
        }
    }

    /**
     * Mit dieser Methode wird der Zug eines Spielers beendet.
     * Laut den Regeln von KingsInTheCorner, muss man ein König spielen, sobald man diesen zieht.
     * Bei unserer Implementierung wird dies automatisiert. Sobald man seinen Zug beendet, dann zieht man eine Karte.
     * Ist es ein König dann wird diese automatisch gespielt.
     * Hat der Spieler jedoch einen König in der Hand, den er noch nicht gespielt hat. Dann muss dieser laut
     * den Regeln 3 Chips an den Spielraum zahlen. (Es kann sein das man beim Beginn vom Spiel einen König bekommt,
     * dann wird es nicht automatisch gespielt)
     * Hat man keine Karten gespielt dann muss einen Chip zahlen.
     * @param gameRoom der Spielraum worin gespielt wird.
     * @throws NoMoreCardsInDeckException falls keine Karten mehr im Deck, in dem gegebenen Spielraum, vorhanden sind.
     */
    public void endTurnOfPlayer(GameRoom gameRoom) throws NoMoreCardsInDeckException{
        try{
            if(!gameRoom.getDeck().isEmpty()){
                if(gameRoom.getDeck().getTopCard().isKing()){
                    Card card = gameRoom.getDeck().drawTopCard();
                    for (Map.Entry<String, Stall> entry : gameRoom.getGameStalls().entrySet()) {
                        String name = entry.getKey();
                        Stall stall = entry.getValue();
                        if (stall.isKingStall() && stall.isEmpty()) {
                            try {
                                stall.addCardToStall(card);
                                gameRoom.getGameStalls().replace(name, stall);
                                break;
                            } catch (IllegalMoveException e) {
                                LOGGER.log(Level.SEVERE, "Ungültiger Zug!");
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                addCardToHand(gameRoom.getDeck().drawTopCard());
            }
            if (hasKing()) {
                for (int i = 0; i < hand.size(); i++) {
                    if (hand.get(i).isKing()) {
                        gameRoom.getPot().addChips(payChips(3));
                    }
                }
            }
        } catch (NoMoreCardsInDeckException e) {
            LOGGER.log(Level.SEVERE, "Es befinden sich keine Karten mehr im Deck");
            throw new NoMoreCardsInDeckException("Es befinden sich keine Karten mehr im Deck");
        }
        if (getPlayedCards() == 0){
            gameRoom.getPot().addChips(payChips(1));
        }
        resetPlayedCards();
        setPlayersTurn(false);
    }

    public boolean isDealer() {
        return dealer;
    }

    public void makeDealer() {
        dealer = true;
    }

    public int getChips() {
        return chips;
    }

    public int getScore() {
        return score;
    }

    public void undoDealer() {
        dealer = false;
    }

    public boolean hasChips() {
        return (chips > 0);
    }

}
