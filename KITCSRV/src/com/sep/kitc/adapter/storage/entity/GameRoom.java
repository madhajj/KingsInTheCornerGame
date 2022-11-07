package com.sep.kitc.adapter.storage.entity;

import com.sep.kitc.adapter.storage.service.RegistrationService;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Logger;


public class GameRoom implements Serializable {

    private static Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());
    private final String gameName;
    private final String gamePassword;
    private final int maxPlayers;
    private Pot pot;

    /**
     * Hier werden die UserIDs bzw. die Benutzerkennungen der Spieler,
     * die sich in dem Moment im Spielraum befinden, gespeichert.
     */
    private ArrayList<String> playerSet;

    /**
     * Hier werden die UserIDs bzw. die Benutzerkennungen der Spieler,
     * die den Spielraum verlassen haben, gespeichert.
     * Wenn diese nun wieder beitreten, dann können die erst spielen, wenn
     * die Runde beendet worden ist.
     */
    private ArrayList<String> waitingList;
    private Deck deck;

    /**
     * In dieser HashMap werden die Stapel mit deren Stapelnamen eingespeichert.
     * Somit kann man später einfach Karten auf einen bestimmten Stapel drauflegen.
     * Die Stapel haben folgende Namen: northStall, northeastKingsStall, eastStall, ...
     */
    private HashMap<String, Stall> gameStalls;
    private boolean round;
    private boolean game;
    private int botCounter;

    /**
     * Beim Erstellen eines Spielraumes, braucht man den Spielraumnamen, Spielraumpasswort sowie die maximale Anzahl
     * an Spielern. Zudem werden die Variablen "round" und "game" auf "false" gesetzt. Dies besagt, dass weder das Spiel
     * noch die Runde angefangen hat. Des Weiteren wird dem Spielraum ein Topf hinzugefügt. Dieser dient Chipspeicher.
     * Unter anderem, erhält ein neuer Spielraum ein neues Deck mit 52 Karten.
     * Weiterhin, wird die Anzahl an Bots auf 0 gesetzt.
     * Zum Schluss, wird die gameStalls HashMap, mit den von uns vorgegebenen Stapelnamen, gefüllt.
     * Die Stapel sind anfangs leer.
     * @param pGameName der Spielraumname.
     * @param pGamePassword das Spielraumpasswort.
     * @param pMaxPlayers die maximale Anzahl an Spielern.
     */
    public GameRoom(String pGameName, String pGamePassword, int pMaxPlayers) {
        gameName = pGameName;
        gamePassword = pGamePassword;
        maxPlayers = pMaxPlayers;
        botCounter = 0;
        round = false;
        game = false;
        pot = new Pot();
        gameStalls = new HashMap<>();
        gameStalls.put("northStall", new Stall());
        gameStalls.put("northeastKingsStall", new Stall());
        gameStalls.get("northeastKingsStall").setKingsStall(true);
        gameStalls.put("eastStall", new Stall());
        gameStalls.put("southeastKingsStall", new Stall());
        gameStalls.get("southeastKingsStall").setKingsStall(true);
        gameStalls.put("southStall", new Stall());
        gameStalls.put("southwestKingsStall", new Stall());
        gameStalls.get("southwestKingsStall").setKingsStall(true);
        gameStalls.put("westStall", new Stall());
        gameStalls.put("northwestKingsStall", new Stall());
        gameStalls.get("northwestKingsStall").setKingsStall(true);
        playerSet = new ArrayList<>();
        waitingList = new ArrayList<>();
        deck = new Deck();
    }

    public void setBotCounter(int botCounter) {
        this.botCounter = botCounter;
    }

    /**
     * Eine Methode, die die Runde zurücksetzt und alle Stalls leert.
     * Zudem, erstellt es ein neues Deck bzw. füllt es wieder auf.
     */
    public void resetCards(){
        gameStalls.replace("northStall", new Stall());
        gameStalls.replace("northeastKingsStall", new Stall());
        gameStalls.get("northeastKingsStall").setKingsStall(true);
        gameStalls.replace("eastStall", new Stall());
        gameStalls.replace("southeastKingsStall", new Stall());
        gameStalls.get("southeastKingsStall").setKingsStall(true);
        gameStalls.replace("southStall", new Stall());
        gameStalls.replace("southwestKingsStall", new Stall());
        gameStalls.get("southwestKingsStall").setKingsStall(true);
        gameStalls.replace("westStall", new Stall());
        gameStalls.replace("northwestKingsStall", new Stall());
        gameStalls.get("northwestKingsStall").setKingsStall(true);
        deck = new Deck();
    }

    /**
     * Eine Methode, um die Anzahl der Bots zu erhöhen.
     */
    public void increaseBotCounter(){
        botCounter++;
    }

    public boolean hasGameStarted() {
        return game;
    }

    public boolean hasRoundStarted() {
        return round;
    }

    public void startGame() {
        game = true;
    }

    public void startRound() {
        round = true;
    }

    public void finishGame() {
        game = false;
    }

    public void finishRound() {
        round = false;
    }

    public int getBotCounter() {
        return botCounter;
    }

    public Deck getDeck() {
        return deck;
    }

    public Pot getPot() {
        return pot;
    }

    public HashMap<String, Stall> getGameStalls() {
        return gameStalls;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGamePassword(){
        return gamePassword;
    }

    public int getMaxPlayers(){
        return maxPlayers;
    }

    public ArrayList<String> getPlayerSet(){
        return playerSet;
    }

    public ArrayList<String> getWaitingList(){
        return waitingList;
    }

    /**
     * Diese Methode fügt einen Spieler der Spielerliste hinzu.
     * @param userIdent die Benutzerkennung des Spielers der beigetreten ist.
     */
    public void addPlayer(String userIdent){
        playerSet.add(userIdent);
    }

    /**
     * Diese Methode fügt einen Spieler der Spielerliste hinzu,
     * wenn der Spieler darauf wartet bis das Spiel wieder anfängt.
     * @param userIdent die Benutzerkennung des Spielers der beigetreten ist.
     */
    public void addPlayerToWaitingList(String userIdent){
        waitingList.add(userIdent);
    }

    /**
     * Diese Methode entfernt einen Spieler aus der Spielerliste.
     * @param userIdent die Benutzerkennung des Spielers der beigetreten ist.
     */
    public void removePlayer(String userIdent) {
        playerSet.remove(userIdent);
    }

    /**
     * Diese Methode entfernt einen Spieler aus der Spielerliste,
     * wenn der Spieler dem Spielraum wieder beigetretren ist.
     * @param userIdent die Benutzerkennung des Spielers der beigetreten ist.
     */
    public void removePlayerFromWaitingList(String userIdent){
        waitingList.remove(userIdent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRoom gameRoom = (GameRoom) o;
        return Objects.equals(gameName, gameRoom.gameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameName);
    }
}
