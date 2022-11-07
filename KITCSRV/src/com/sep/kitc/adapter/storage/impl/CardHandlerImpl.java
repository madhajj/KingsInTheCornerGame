package com.sep.kitc.adapter.storage.impl;

import com.sep.kitc.adapter.storage.entity.*;
import com.sep.kitc.adapter.storage.service.CreateGameRoomService;
import com.sep.kitc.adapter.storage.service.RegistrationService;
import com.sep.kitc.common.CardHandler;
import com.sep.kitc.common.exception.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CardHandlerImpl implements CardHandler {

    /**
     * Dies ist unser Logger. Dies wird als debugging Hilfsmittel verwendet.
     * Jedes Mal, wenn eine Fehlermeldung auftreten sollte, dann wird diese mithilfe
     * des Loggers gespeichert.
     */
    private static Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());

    /**
     * Dies ist unser gameRoomService Objekt. Es wird verwendet um mithilfe des Spielraumnamen,
     * das Spielraum Objekt zu bekommen. Somit können wir dann auf Variablen und Methoden in diesem Spielraum
     * zugreifen.
     */
    private CreateGameRoomService gameRoomService;

    /**
     * Dies ist unser RegistrationService Objekt. Es wird verwendet um mithilfe der einzigartigen UserID,
     * bzw. Benutzerkennung das dazu passende Spieler Objekt zu bekommen. Somit können wir dann auf Variablen und Methoden
     * von diesem Spieler zugreifen.
     */
    private RegistrationService registry;

    /**
     * Bei dieser HashMap handelt es sich um einen Dolmetscher, der sich darum kümmert, dass sich Client und Server
     * richtig verstehen. Konkret werden Kartenobjekte und deren Namen als String gespeichert. Somit kann man nur mit einem
     * String herausfinden um welches Kartenobjekt es sich handelt. Wir verwenden dies, da sich Client und Server nur mit
     * Strings austauschen.
     */
    private HashMap<String, Card> cardStorage;

    /**
     * Bei dieser HashMap handelt es sich um die Bestenliste. Diese speichert den Benutzernamen und den dazugehörigen
     * Score.
     */
    private static HashMap<String, Integer> rankList;

    /**
     * Dies ist der Konstrukteur der Klasse. Hier werden alle benötigten Objekt erstellt.
     * Zuletzt wird die cardStorage HashMap, mit allen möglichen Karten gefüllt.
     */
    public CardHandlerImpl(){
        gameRoomService = new CreateGameRoomService();
        registry = new RegistrationService();
        cardStorage = new HashMap<>();
        rankList = new HashMap<>();
        fillCardStorage();
    }


    /**
     * Die cardStorage HashMap muss gefüllt werden, damit diese uns von Nutzen ist. Genau das übernimmt diese Methode.
     * Es wird ein Deck erstellt, mit den bereits bekannten 52 Karten. Diese werden dann nacheinander in die HashMap eingetragen.
     * Zuerst mit dem Namen als String als Key und als Value das dazugehörige Karten Objekt.
     */
    public synchronized void fillCardStorage(){
        Deck deck = new Deck();
        while(deck.getNumberOfCards() > 0){
            try {
                cardStorage.put(deck.getTopCard().toString(), deck.drawTopCard());
            } catch (NoMoreCardsInDeckException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Diese Methode verteilt die Chips unter den Spielern in einem Spielraum. Es nimmt
     * den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Nach den Regeln von KingsInTheCorner müssen die 80 Chips so gleichmäßig wie möglich an die
     * Spieler verteilt werden. Deswegen teilen wir die Anzahl an Chips durch die Anzahl an maximalen Spielern
     * in dem gegebenen Spielraum.
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized void distributeChips(String gameRoomName) throws RemoteException {
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            gameRoom.getPlayerSet().forEach(ID -> {
                try {
                    RegistrationService.getRegisteredPlayer(ID).addChips(80/gameRoom.getMaxPlayers());
                } catch (UserNotCreatedException e) {
                    LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung gibt es nicht");
                    throw new RuntimeException(e);
                }
            });
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode verteilt die Karten unter den Spielern und auf der Spielfläche,
     * in dem gegebenen Spielraum. Es nimmt den Spielraumnamen als Parameter.
     * Dieser wird dann verwendet, um, mithilfe des gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Zudem nimmt diese Methode die spezifische Benutzerkennung bzw. die UserID, als Parameter auf.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * Zum Schluss wird überprüft ob dieser Spieler dann der Dealer ist.
     * Falls ja, dann verteilt dieser die Karten.
     * Falls nein, dann wird das If-Statement übersprungen und es wird nichts ausgeführt.
     * Diese Methode wird von mehreren Clients aufgerufen, deswegen müssen wir sicherstellen, dass nur der Dealer
     * die Karten verteilen kann.
     * Nach den Regeln von KingsInTheCorner werden 7 Karten pro Spieler verteilt (die Karten werden in Hand des Spielers
     * gelegt) und 4 Karten auf die Spielfläche,
     * wobei es sich um keinen Königsstapel handeln darf.
     * @param gameRoomName der Spielraumname
     * @param userID die Benutzerkennung des eventuellen Dealers
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized void distributeCards(String gameRoomName, String userID) throws RemoteException{
        try {
            if (RegistrationService.getRegisteredPlayer(userID).isDealer()){
                GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
                ArrayList<String> playersInRoom = gameRoom.getPlayerSet();
                playersInRoom.forEach(Id -> {
                    try {
                        int i = 0;
                        while(i < 7){
                            RegistrationService.getRegisteredPlayer(Id).addCardToHand(gameRoom.getDeck().drawTopCard());
                            i++;
                        }
                    } catch (UserNotCreatedException e) {
                        LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
                        throw new RuntimeException(e);
                    } catch (NoMoreCardsInDeckException e) {
                        LOGGER.log(Level.SEVERE, "Es befinden sich keine Karten im Deck");
                        throw new RuntimeException(e);
                    }
                });
                gameRoom.getGameStalls().forEach((name, stall) -> {
                    if (!stall.isKingStall()){
                        try {
                            stall.addCardToStall(gameRoom.getDeck().drawTopCard());
                        } catch (NoMoreCardsInDeckException e) {
                            LOGGER.log(Level.SEVERE, "Es befinden sich keine Karten im Deck");
                            throw new RuntimeException(e);
                        } catch (IllegalMoveException e) {
                            LOGGER.log(Level.SEVERE, "Diese Karte kann man nicht spielen");
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode prüft, ob jemand die Runde in einem gewissen Spielraum gewonnen hat.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Danach wird über die Spielerliste in diesem Spielraum iteriert und überprüft, ob die Variable "wonRound", von
     * einem Spieler, auf "true" steht. Falls es einen Spieler mit diesen Bedingungen geben sollte, dann terminiert die Schleife
     * direkt und es wird "true" zurückgegeben.
     * Ansonsten wird "false" zurückgegeben.
     * @param gameRoomName der Spielraumname
     * @return "true", falls es einen Spieler mit den benötigten Bedingungen geben sollte, ansonsten "false".
     * @throws RemoteException falls die Verbindung zum Server unterbrochen worden ist.
     */
    public synchronized Boolean hasSomeoneWon(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID :gameRoom.getPlayerSet()){
                RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                if (player.getHand().isEmpty() && gameRoom.hasRoundStarted()){
                    player.setWonRound(true);
                    return true;
                }
            }
            return false;
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode überprüft, ob ein Spieler das Spiel gewonnen hat.
     * @param gameRoomName der Spielraumname.
     * @return "true", falls dies der Fall ist, ansonsten "false".
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized Boolean hasSomeOneWonGame(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID : gameRoom.getPlayerSet()){
                RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                if (player.getScore() >= 100){
                    player.setWonGame(true);
                    return true;
                }
            }
            return false;
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode fügt dem gegebenen Spielraum einen Bot mit einem einfachen Schwierigkeitsgrad hinzu.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized void addEasyBot(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            if (gameRoom.getPlayerSet().size() < gameRoom.getMaxPlayers()){
                String botID = registry.addEasyBotInRegistry(gameRoom);
                gameRoom.addPlayer(botID);
                gameRoom.increaseBotCounter();
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode fügt dem gegebenen Spielraum einen Bot mit einem mittleren Schwierigkeitsgrad hinzu.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized void addMediumBot(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            if (gameRoom.getPlayerSet().size() < gameRoom.getMaxPlayers()){
                String botID = registry.addMediumBotInRegistry(gameRoom);
                gameRoom.addPlayer(botID);
                gameRoom.increaseBotCounter();
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode gibt die Anzahl an Bots in einem gegebenen Spielraum zurück.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @return die Anzahl an Bots in einem Spielraum, als Integer.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized int botsInGameRoom(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            return gameRoom.getBotCounter();
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode startet die Bot-Threads, in dem gegebenen Spielraum.
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized void startTheBots(String gameRoomName) throws RemoteException {
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID : gameRoom.getPlayerSet()) {
                RegisteredPlayer bot = RegistrationService.getRegisteredPlayer(ID);
                if (bot.isBot()) {
                    Thread t1 = new Thread(bot);
                    t1.start();
                }
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode gibt die Anzahl an momentanen Spielern in einem gegebenen Spielraum zurück.
     * (Somit nicht zwingend die maximale Anzahl an Spielern in einem Spielraum).
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @return die momentane Anzahl an Spielern in einem Spielraum, als Integer.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized int getNumberOfPlayersInGameRoom(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            return gameRoom.getPlayerSet().size();
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode prüft, ob jemand die Runde in einem gewissen Spielraum gewonnen hat.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Danach wird über die Spielerliste in diesem Spielraum iteriert und überprüft, ob die Variable "wonRound", von
     * einem Spieler, auf "true" steht. Falls es einen Spieler mit diesen Bedingungen geben sollte, dann terminiert die Schleife
     * direkt und der Benutzername des Spielers wird zurückgegeben.
     * Ansonsten wird null zurückgegeben.
     * @param gameRoomName der Spielraumname
     * @return Benutzernamen des Spielers der gewonnen hat.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen worden ist.
     */
    public String whoWonRound(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID :gameRoom.getPlayerSet()){
                RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                if (player.hasWonRound()){
                    return player.getUsername();
                }
            }
            return null;
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode prüft, ob jemand das Spiel in einem gewissen Spielraum gewonnen hat.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Danach wird über die Spielerliste in diesem Spielraum iteriert und überprüft, ob die Variable "wonRound", von
     * einem Spieler, auf "true" steht. Falls es einen Spieler mit diesen Bedingungen geben sollte, dann terminiert die Schleife
     * direkt und der Benutzername des Spielers wird zurückgegeben.
     * Ansonsten wird null zurückgegeben.
     * @param gameRoomName der Spielraumname
     * @return Benutzernamen des Spielers der gewonnen hat.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen worden ist.
     */
    public String whoWonGame(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID :gameRoom.getPlayerSet()){
                RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                if (player.hasWonGame()){
                    return player.getUsername();
                }
            }
            return null;
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Hier wird die CardStorage HashMap benutzt um Karten zwischen Server und Client austauschen zu können.
     * Client weiß nicht was ein Card Objekt ist, somit wird es als String an den Client gesendet.
     * Wenn der Client nun eine Karte spielt, dann wird diese Karte als String wieder an den Server zurückgesandt.
     * Der Server kann mit einem String einer Karte nichts anfangen, weswegen es diese Methode aufruft, um mithilfe des Namens
     * das dazugehörige Card Objekt zu bekommen.
     * @param cardName Name der Karte
     * @return das Kartenobjekt was zu diesem Kartennamen gehört.
     */
    public synchronized Card getCardOf(String cardName){
        return cardStorage.get(cardName);
    }

    /**
     * Diese Methode gibt die Hand eines Spielers zurück. Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * Damit können wir dann auf die Hand des Spielers zugreifen.
     * Nun können die Karten zurückgegeben werden, jedoch weiß der Client nicht was ein Card Objekt ist,
     * somit werden die Karten erst in Strings konvertiert. Diese Strings werden dann in einer ArrayListe gespeichert.
     * Diese wird dann zurückgegeben.
     * @param userID die Benutzerkennung eines Spielers.
     * @return ArrayListe mit den Kartennamen als Strings. Diese repräsentiert die Hand des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized ArrayList<String> getHandOfPlayer(String userID) throws RemoteException{
        try {
            ArrayList<String> hand = new ArrayList<>();
            RegistrationService.getRegisteredPlayer(userID).getHand().forEach(card -> hand.add(card.toString()));
            return hand;
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Hier werden alle Karten aus der Hand eines Spielers entfernt.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * Damit können wir dann auf die Hand des Spielers zugreifen und diese dann leeren.
     * @param userID die Benutzerkennung eines Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized void emptyThePlayersHand(String userID) throws RemoteException{
        try {
            RegistrationService.getRegisteredPlayer(userID).getHand().clear();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode gibt die alle Stapel der Spielfläche aus einem gegebenen Spielraum,
     * als HashMap, zurück. Wobei Key hier der Name des Stapels ist und die Value die Karten die auf dem Stapel liegen.
     * Diese Methode ist sehr wichtig, da es verwendet wird, um die Karten beim Client später darstellen zu können.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Auf der Serverseite sind Stapel als "Stall" und Karten als "Card" Objekte bekannt.
     * Der Client kann jedoch nur mit Strings arbeiten. Somit wird ein Stapel in eine ArrayListe von Strings gespeichert.
     * In dieser ArrayListe ist einmal die "TopCard" und die "BottomCard" gespeichert, da der Client nur diese braucht.
     * Ist der Stall leer, dann wird als "TopCard" ein leerer String gespeichert und als "BottomCard" die leere Karte.
     * Somit weiß der Client, mithilfe der HashMap, nun auf was für einem Stapel was für eine Karte liegt.
     * @param gameRoomName den Spielraumnamen.
     * @return die HashMap wo der Stapelname als Key und die ArrayListe mit den Strings als Value gespeichert worden ist.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized HashMap<String, ArrayList<String>> getTheGameStalls(String gameRoomName) throws RemoteException{
        try {
            HashMap<String, ArrayList<String>> tempHash = new HashMap<>();
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            HashMap<String, Stall> stallHashMap = gameRoom.getGameStalls();
            stallHashMap.forEach((name, stall) -> {
                if(!stall.isEmpty()){
                    ArrayList<String> tempArray = new ArrayList<>();
                    tempArray.add(stall.getTopCard().toString());
                    tempArray.add(stall.getBottomCard().toString());
                    tempHash.put(name, tempArray);
                } else {
                    ArrayList<String> tempArray = new ArrayList<>();
                    tempArray.add("");
                    tempArray.add("White Card");
                    tempHash.put(name, tempArray);
                }
            });
            return tempHash;
            } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Hier werden die Chips eines gegebenen Spielers zurückgegeben.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung eines Spielers.
     * @return die momentane Anzahl an Chips des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized int getThePlayersChips(String userID) throws RemoteException{
        try {
            return RegistrationService.getRegisteredPlayer(userID).getChips();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Hier werden der Score eines gegebenen Spielers zurückgegeben.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung eines Spielers.
     * @return der momentane Score des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized int getThePlayersScore(String userID) throws RemoteException{
        try {
            return RegistrationService.getRegisteredPlayer(userID).getScore();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Hier werden der TotaleScore eines gegebenen Spielers zurückgegeben.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung eines Spielers.
     * @return der momentane Score des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized int getThePlayersTotalScore(String userID) throws RemoteException{
        try {
            return RegistrationService.getRegisteredPlayer(userID).getTotalScore();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode gibt die momentane Anzahl an Chips in einem gegebenen Spielraum.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @return die momentane Anzahl an Chips in einem gegebenen Spielraum.
     */
    public synchronized int getPotChips(String gameRoomName){
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            return gameRoom.getPot().getChips();
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode wird benutzt, um nach Ende der Runde Chips an den Topf des Spielraums zu zahlen.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Nach den Regeln von KingsInTheCorner wird pro Karte, die man in der Hand hat, ein Chip gezahlt.
     * Hat man jedoch mehr Karten in der Hand als man Chips besitzt, dann werden alle Chips gezahlt.
     * @param gameRoomName der Spielraumname
     */
    public synchronized void payChipsToPodAfterRoundEnded(String gameRoomName){
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID : gameRoom.getPlayerSet()){
                RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                int cardsStillInHand = player.getHand().size();
                if ((player.getChips() - cardsStillInHand) >= 0){
                    gameRoom.getPot().addChips(player.payChips(cardsStillInHand));
                } else {
                    gameRoom.getPot().addChips(player.payChips(player.getChips()));
                }
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Hier wird überprüft, ob das Deck in einem Spielraum leer ist.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @return "true" falls es leer sein sollte, ansonsten "false".
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized boolean isDeckOfGameRoomEmpty(String gameRoomName) throws RemoteException{
        try {
            return gameRoomService.getGameRoom(gameRoomName).getDeck().isEmpty();
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode gibt dem gewonnenen Spieler, in dem gegebenen Spielraum, seinen Score bzw. seine Punkte.
     * Der Score wird aus der Anzahl an Chips im Topf des Spielraumes berechnet. Konkret werden die Chips vom dem Topf im
     * Spielraum dem gewonnenen Spieler übergeben. Danach ist der Topf leer.
     * Die Methode nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname.
     */
    public synchronized void giveThePlayerWhoWonRoundHisScore(String gameRoomName){
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID : gameRoom.getPlayerSet()){
                RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                if (player.hasWonRound()){
                    player.addScore(gameRoom.getPot().getChips());
                    player.addToTotalScore(gameRoom.getPot().getChips());
                    gameRoom.getPot().resetPot();
                    break;
                }
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Mit dieser Methode wird die Runde, eines gegebenen Spielraumes beendet.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Es wird über die Spielerliste iteriert. Für jeden Spieler wird zuerst überprüft, ob es ein Bot ist.
     * Falls ja, dann wird der Zug und "wonRound" auf "false" gesetzt. Zudem wird die Hand geleert.
     * Falls nein, dann wird das Gleiche gemacht und der Spieler wird auf "nicht bereit" gestellt.
     * Zum Schluss, wird die Variable, von dem gegebenen Spielraum, "round" auf "false" gesetzt und
     * die Karten der Spielfläche zurückgesetzt.
     * @param gameRoomName der Spielraumname.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized void endRound(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            payChipsToPodAfterRoundEnded(gameRoomName);
            giveThePlayerWhoWonRoundHisScore(gameRoomName);
            for (String ID : gameRoom.getWaitingList()){
                gameRoom.addPlayer(ID);
            }
            for (String ID  : gameRoom.getPlayerSet()){
                RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                if (!player.isBot()){
                    player.setUnReady();
                }
                player.setPlayersTurn(false);
                player.getHand().clear();
                gameRoom.resetCards();
                gameRoom.finishRound();
            }
            setDealer(gameRoomName);
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Mit dieser Methode wird der Zug eines Spielers beendet.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Als weiterer Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * Zum Schluss wird die giveTurn() Methode aufgerufen.
     * @param userID die Benutzerkennung des Spielers
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     * @throws NoMoreCardsInDeckException falls es keine Karten mehr im Deck gibt.
     */
    public synchronized void endTurn(String userID, String gameRoomName) throws RemoteException, NoMoreCardsInDeckException{
        try {
            if (isItThePlayersTurn(userID)){
                RegisteredPlayer player =  RegistrationService.getRegisteredPlayer(userID);
                GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
                player.endTurnOfPlayer(gameRoom);
                if (player.isOut()){
                    for (int i = 0; i < player.getHand().size(); i++){
                        gameRoom.getDeck().addCardToDeck(player.getHand().get(i));
                    }
                    player.getHand().clear();
                    giveTurn(userID, gameRoomName);
                    gameRoom.addPlayerToWaitingList(userID);
                    gameRoom.removePlayer(userID);
                }
                giveTurn(userID, gameRoomName);
            }
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode gibt dem Spieler links vom gegebenen Spieler den Zug.
     * Konkret wird die Variable "turn" von dem links liegenden Spieler auf "true" gesetzt.
     * @param userID die Benutzerkennung des Spielers.
     * @param gameRoomName den Spielraumnamen.
     */
    public void giveTurn(String userID, String gameRoomName){
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            int i = 0;
            for (String ID : gameRoom.getPlayerSet()) {
                if (ID.equals(userID)) {
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

        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode setzt den Zug von dem gegebenen Spieler auf "true"
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized void startTurn(String userID) throws RemoteException{
        try {
            RegistrationService.getRegisteredPlayer(userID).setPlayersTurn(true);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode entfernt einen Bot aus dem gegebenen Spielraum.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized void removeBot(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID : gameRoom.getPlayerSet()){
                RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                if (player.isBot()){
                    registry.removeBot(player);
                    gameRoom.setBotCounter(gameRoom.getBotCounter() - 1);
                    gameRoom.getPlayerSet().remove(player.getUserIdent());
                    break;
                }
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode überprüft, ob der gegebene Spieler am Zug ist.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @return "true", falls der Spieler am Zug ist, "false" wenn nicht.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized boolean isItThePlayersTurn(String userID) throws RemoteException{
        try {
            return RegistrationService.getRegisteredPlayer(userID).isPlayersTurn();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Mit dieser Methode kann ein Client bzw. Spieler eine Karte spielen.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * Zudem nimmt es den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Außerdem wird der Stapelname, von dem Stapel worauf man spielen will, benötigt.
     * Zum Schluss, benötigt die Methode noch den Kartennamen von der Karte, die der Spieler spielen will.
     * Stapelnamen sowie Kartennamen sind als String gegeben, somit müssen diese erst in die jeweiligen Objekte transformiert werden.
     * Dies machen wir mit der CardHandler HashMap. Den Stall bekommen wir durch die im Spielraum definierte gameStalls HashMap.
     * Diese Parameter werden dann, dem Spieler Objekt der diese Karte spielen möchte, in seine playCard() Methode übergeben.
     * @param userID die Benutzerkennung des Spielers.
     * @param gameRoomName der Spielraumname.
     * @param stallName der Stapelname von dem Stapel worauf man die Karte spielen möchte.
     * @param cardName der Kartenname von der Karte die man gerne Spielen möchte.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     * @throws NotThePlayersTurnException falls es nicht am Spielers zug ist.
     * @throws IllegalMoveException falls es sich bei diesem Zug um einen ungültigen Zug handelt.
     */
    public synchronized void playThisCard(String userID, String gameRoomName, String stallName, String cardName) throws RemoteException, NotThePlayersTurnException, IllegalMoveException {
        try {
            RegisteredPlayer registeredPlayer = RegistrationService.getRegisteredPlayer(userID);
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            Stall stall = gameRoom.getGameStalls().get(stallName);
            Card card = getCardOf(cardName);
            registeredPlayer.playCard(card, stall);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Mit dieser Methode kann ein Client bzw. Spieler ein Stapel auf einen anderen legen.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * Zudem nimmt es den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Außerdem wird der Stapelname von dem ersten Stapel benötigt.
     * Zum Schluss, benötigt die Methode den Stapelnamen von dem zweiten Stapel, worauf man den ersten Stapel spielen will.
     * Stapelnamen sind als String gegeben, somit müssen diese erst in die jeweiligen Objekte transformiert werden.
     * Dies bekommen wir durch die im Spielraum definierte gameStalls HashMap.
     * Diese Parameter werden dann der mergeStalls() Methode des ersten Stapels übergeben.
     * @param userID die Benutzerkennung des Spielers.
     * @param gameRoomName der Spielraumname.
     * @param stallName1 der Stapelname von dem ersten Stapel
     * @param stallName2 der Stapelname von dem zweiten Stapel, worauf man den ersten Stapel spielen möchte.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     * @throws IllegalMoveException falls es sich bei diesem Zug um einen ungültigen Zug handelt.
     */
    public synchronized void mergeTheseStalls(String userID, String gameRoomName, String stallName1, String stallName2) throws RemoteException, IllegalMoveException {
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            gameRoom.getGameStalls().get(stallName1).mergeStalls(gameRoom.getGameStalls().get(stallName2));
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode überprüft, ob der gegebene Spieler bereit ist.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @return "true", falls der Spieler bereit ist, "false" wenn nicht.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized boolean isPlayerReady(String userID) throws RemoteException{
        try {
            return RegistrationService.getRegisteredPlayer(userID).isReady();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode setzt den gegebenen Spieler auf bereit.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized void switchPlayerToReady(String userID) throws RemoteException{
        try {
            RegistrationService.getRegisteredPlayer(userID).setReady();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode setzt den gegebenen Spieler auf nicht bereit.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized void setPlayerToUnready(String userID) throws RemoteException{
        try {
            RegistrationService.getRegisteredPlayer(userID).setUnReady();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode überprüft, jeder Spieler in dem gegebenen Spielraum bereit ist.
     * Wir iterieren durch die Spielerliste des Spielraumes. Ist ein Spieler nicht bereit dann wird "false"
     * zurück gegebenen, ansonsten "true".
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName den Spielraumnamen.
     * @return "true" falls alle Spieler bereit sind, "false" wenn nicht.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized boolean isEveryPlayerReady(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String player : gameRoom.getPlayerSet()){
                if (!isPlayerReady(player)){
                    return false;
                }
            }
            return true;
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode überprüft, ob der gegebene Spieler ein Dealer ist.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @return "true", falls der Spieler ein Dealer ist, "false" wenn nicht.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized boolean isDealer(String userID) throws RemoteException{
        try {
            return RegistrationService.getRegisteredPlayer(userID).isDealer();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode überprüft, ob die Runde in dem gegebenen Spielraum gestartet ist.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameNameRoom den Spielraumnamen.
     * @return die "round" Variable aus dem Spielraum.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized boolean hasRoundStarted(String gameNameRoom) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameNameRoom);
            return gameRoom.hasRoundStarted();
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode überprüft, ob das Spiel in dem gegebenen Spielraum gestartet ist.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName den Spielraumnamen.
     * @return die "game" Variable aus dem Spielraum.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized boolean hasGameStarted(String gameRoomName) throws RemoteException {
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            return gameRoom.hasGameStarted();
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode gibt die Anzahl an Spielern zurück, die im Moment bereit sind.
     * Wir iterieren durch die Spielerliste des Spielraumes. Ist ein Spieler bereit dann wird
     * der Zähler erhöht.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName den Spielraumnamen.
     * @return die Anzahl an Spielern zurück, die im Moment bereit sind.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized int howManyPlayersAreReady(String gameRoomName) throws RemoteException{
        try {
            int c = 0;
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String player : gameRoom.getPlayerSet()){
                if (RegistrationService.getRegisteredPlayer(player).isReady()){
                    c++;
                }
            }
            return c;
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode beendet das Spiel, nachdem die maximale Punktzahl erreicht wurde.
     * Laut den Regeln von KingsInTheCorner, hört das Spiel auf, sobald ein Spieler mehr als 100 Punkte hat.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Zum Schluss, wir die Variable "game" aus dem gegebenen Spielraum auf "false" gesetzt und die Methode
     * endRound() wird aufgerufen.
     * @param gameRoomName der Spielraumnamen.
     */
    public synchronized void endGame(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            gameRoom.finishGame();
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Hier wird das Spiel in dem gegebenen Spielraum gestartet.
     * Die Methode nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Die beiden Variablen "round" sowie "game" werden auf "true" gesetzt.
     * Die StartTurn() Methode wird aufgerufen.
     * Zum Schluss, muss, laut den Regeln von KingsInTheCorner, jeder Spieler, beim beginn des Spiels,
     * einen Chip an den Spielraum zahlen.
     * @param gameRoomName den Spielraumnamen.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized void startGame(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            gameRoom.startGame();
            gameRoom.startRound();
            startTurn(gameRoom.getPlayerSet().get(1));
            for (String ID : gameRoom.getPlayerSet()){
                gameRoom.getPot().addChips(RegistrationService.getRegisteredPlayer(ID).payChips(1));
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode überprüft, ob die maximale Anzahl an Spielern in dem gegebenen Spielraum erreicht worden ist.
     * Die Methode nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Falls es erreicht worden ist, dann wird "true" zurückgegeben, ansonsten wird eine Exception geworfen.
     * @param gameRoomName der Spielraumname
     * @return "true", falls die maximale Anzahl an Spielern in dem gegebenen Spielraum erreicht worden ist.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     * @throws NotAllPlayersJoinedException falls noch nicht alle Spieler beigetreten sind.
     */
    public synchronized boolean isMaxPlayersReached(String gameRoomName) throws RemoteException, NotAllPlayersJoinedException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            if (gameRoom.getPlayerSet().size()  == gameRoom.getMaxPlayers()){
                return true;
            } else {
                throw new NotAllPlayersJoinedException("Die maximale Anzahl an Spielern ist noch nicht erreicht");
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode setzt den Spieler, der zuerst beigetreten ist als Dealer.
     * Die Methode nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized void setDealer(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID : gameRoom.getPlayerSet()){
                RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                if (!player.isBot()){
                    player.makeDealer();
                    break;
                }
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode entzieht dem gegebenen Spieler den Dealer Status.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    public synchronized void undoDealer(String userID) throws RemoteException{
        try {
            RegistrationService.getRegisteredPlayer(userID).undoDealer();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode setzt den Gewinner Status, für jeden Spieler in dem gegebenen Spielraum, zurück.
     * Die Methode nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    public synchronized void resetWinner(String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID : gameRoom.getPlayerSet()){
                RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                player.setWonRound(false);
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode entfernt einen Spieler aus einem Spielraum.
     * Zudem fügt es die Karten des Spielers zurück in das Deck des Spielraumes.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Zudem wird die spezifische Benutzerkennung bzw. die UserID als Parameter erwartet.
     *
     * @param gameRoomName der Spielraumname.
     * @param userID       die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    @Override
    public synchronized void removePlayerOutOfGameRoom(String gameRoomName, String userID) throws RemoteException {
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            for (String ID : gameRoom.getPlayerSet()){
                if (ID.equals(userID)){
                    RegisteredPlayer player = RegistrationService.getRegisteredPlayer(ID);
                    if (player.isPlayersTurn()){
                        giveTurn(userID, gameRoomName);
                    }
                    for (int i = 0; i < player.getHand().size(); i++){
                        gameRoom.getDeck().addCardToDeck(player.getHand().get(i));
                    }
                    gameRoom.getPot().addChips(player.payChips(player.getChips()));
                    gameRoom.getPlayerSet().remove(ID);
                    gameRoom.getWaitingList().remove(ID);
                    break;
                }
            }
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Diese Methode setzt alle Attribute des Spieler objekts zurück.
     * Es wird die spezifische Benutzerkennung bzw. die UserID als Parameter erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     *
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    @Override
    public synchronized void resetPlayer(String userID) throws RemoteException {
        try {
            RegisteredPlayer player = RegistrationService.getRegisteredPlayer(userID);
            player.getHand().clear();
            player.undoDealer();
            player.setOut(false);
            player.setWonRound(false);
            player.setPlayersTurn(false);
            player.setUnReady();
            player.setChips(0);
            player.setScore(0);
            player.resetPlayedCards();
        } catch (UserNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung existiert nicht");
            throw new RuntimeException(e);
        }
    }

    /**
     * Überprüft, ob ein Spieler aus dem Spiel rausgeflogen ist.
     * @param userID die Benutzerkennung des gegebenen Spielers.
     * @param gameRoomName der Spielraumname worin sich der Spieler gerade befindet.
     * @return "true", falls dies der Fall sein sollte, ansonsten "false".
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    @Override
    public synchronized boolean isPlayerOut(String userID, String gameRoomName) throws RemoteException{
        try {
            GameRoom gameRoom = gameRoomService.getGameRoom(gameRoomName);
            return !gameRoom.getPlayerSet().contains(userID);
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new RuntimeException(e);
        }
    }
}
