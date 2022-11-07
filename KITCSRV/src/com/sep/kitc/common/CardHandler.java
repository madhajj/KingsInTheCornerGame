package com.sep.kitc.common;
import com.sep.kitc.common.exception.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Diese Schnittstelle kümmert sich um alle Funktionalitäten, die man braucht,
 * um das Spiel spielen zu können.
 */
public interface CardHandler extends Remote, Serializable {


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
    void distributeChips(String gameRoomName) throws RemoteException;

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
    void distributeCards(String gameRoomName, String userID) throws RemoteException;

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
    Boolean hasSomeoneWon(String gameRoomName) throws RemoteException;

    /**
     * Diese Methode überprüft, ob ein Spieler das Spiel gewonnen hat.
     * @param gameRoomName der Spielraumname.
     * @return "true", falls dies der Fall ist, ansonsten "false".
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    Boolean hasSomeOneWonGame(String gameRoomName) throws RemoteException;

    /**
     * Diese Methode fügt dem gegebenen Spielraum einen Bot mit einem einfachen Schwierigkeitsgrad hinzu.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    void addEasyBot(String gameRoomName) throws RemoteException;


    /**
     * Diese Methode fügt dem gegebenen Spielraum einen Bot mit einem mittleren Schwierigkeitsgrad hinzu.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    void addMediumBot(String gameRoomName) throws RemoteException;


    /**
     * Diese Methode gibt die Anzahl an Bots in einem gegebenen Spielraum zurück.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @return die Anzahl an Bots in einem Spielraum, als Integer.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    int botsInGameRoom(String gameRoomName) throws RemoteException;

    /**
     * Diese Methode startet die Bot-Threads, in dem gegebenen Spielraum.
     * Zum Schluss ruft es die giveTurn() Methode auf um den Zug an den Spieler rechts
     * von dem gegebenen Spieler weiterzugeben.
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    void startTheBots(String gameRoomName) throws RemoteException;

    /**
     * Diese Methode gibt die Anzahl an momentanen Spielern in einem gegebenen Spielraum zurück.
     * (Somit nicht zwingend die maximale Anzahl an Spielern in einem Spielraum).
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @return die momentane Anzahl an Spielern in einem Spielraum, als Integer.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    int getNumberOfPlayersInGameRoom(String gameRoomName) throws RemoteException;

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
    String whoWonRound(String gameRoomName) throws RemoteException;

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
    String whoWonGame(String gameRoomName) throws RemoteException;

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
    ArrayList<String> getHandOfPlayer(String userID) throws RemoteException;

    /**
     * Hier werden alle Karten aus der Hand eines Spielers entfernt.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * Damit können wir dann auf die Hand des Spielers zugreifen und diese dann leeren.
     * @param userID die Benutzerkennung eines Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    void emptyThePlayersHand(String userID) throws RemoteException;

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
    HashMap<String, ArrayList<String>> getTheGameStalls(String gameRoomName) throws RemoteException;

    /**
     * Hier werden die Chips eines gegebenen Spielers zurückgegeben.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung eines Spielers.
     * @return die momentane Anzahl an Chips des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    int getThePlayersChips(String userID) throws RemoteException;


    /**
     * Hier werden der Score eines gegebenen Spielers zurückgegeben.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung eines Spielers.
     * @return der momentane Score des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    int getThePlayersScore(String userID) throws RemoteException;

    /**
     * Hier werden der TotaleScore eines gegebenen Spielers zurückgegeben.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung eines Spielers.
     * @return der momentane Score des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    int getThePlayersTotalScore(String userID) throws RemoteException;

    /**
     * Hier wird überprüft, ob das Deck in einem Spielraum leer ist.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @return "true" falls es leer sein sollte, ansonsten "false".
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    boolean isDeckOfGameRoomEmpty(String gameRoomName) throws RemoteException;

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
    void endRound(String gameRoomName) throws RemoteException;

    /**
     * Mit dieser Methode wird der Zug eines Spielers beendet.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Als weiterer Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * Zum Schluss wird der Zug an dem Spieler rechts von dem gegebenen Spieler weitergegeben.
     * @param userID die Benutzerkennung des Spielers
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     * @throws NoMoreCardsInDeckException falls es keine Karten mehr im Deck gibt.
     */
    void endTurn(String userID, String gameRoomName) throws RemoteException, NoMoreCardsInDeckException;

    /**
     * Diese Methode setzt den Zug von dem gegebenen Spieler auf "true"
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    void startTurn(String userID) throws RemoteException;

    /**
     * Diese Methode entfernt einen Bot aus dem gegebenen Spielraum.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    void removeBot(String gameRoomName) throws RemoteException;

    /**
     * Diese Methode überprüft, ob der gegebene Spieler am Zug ist.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @return "true", falls der Spieler am Zug ist, "false" wenn nicht.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    boolean isItThePlayersTurn(String userID) throws RemoteException;

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
    void playThisCard(String userID, String gameRoomName, String stallName, String cardName) throws RemoteException, NotThePlayersTurnException, IllegalMoveException;

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
    void mergeTheseStalls(String userID, String gameRoomName, String stallName1, String stallName2) throws RemoteException, IllegalMoveException;

    /**
     * Diese Methode überprüft, ob der gegebene Spieler bereit ist.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @return "true", falls der Spieler bereit ist, "false" wenn nicht.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    boolean isPlayerReady(String userID) throws RemoteException;

    /**
     * Diese Methode setzt den gegebenen Spieler auf bereit.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    void switchPlayerToReady(String userID) throws RemoteException;

    /**
     * Diese Methode setzt den gegebenen Spieler auf nicht bereit.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    void setPlayerToUnready(String userID) throws RemoteException;

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
    boolean isEveryPlayerReady(String gameRoomName) throws RemoteException;

    /**
     * Diese Methode überprüft, ob der gegebene Spieler ein Dealer ist.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @return "true", falls der Spieler ein Dealer ist, "false" wenn nicht.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    boolean isDealer(String userID) throws RemoteException;

    /**
     * Diese Methode überprüft, ob die Runde in dem gegebenen Spielraum gestartet ist.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameNameRoom den Spielraumnamen.
     * @return die "round" Variable aus dem Spielraum.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    boolean hasRoundStarted(String gameNameRoom) throws RemoteException;

    /**
     * Diese Methode überprüft, ob das Spiel in dem gegebenen Spielraum gestartet ist.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName den Spielraumnamen.
     * @return die "game" Variable aus dem Spielraum.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    boolean hasGameStarted(String gameRoomName) throws RemoteException;

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
    int howManyPlayersAreReady(String gameRoomName) throws RemoteException;

    /**
     * Diese Methode beendet das Spiel, nachdem die maximale Punktzahl erreicht wurde.
     * Laut den Regeln von KingsInTheCorner, hört das Spiel auf, sobald ein Spieler mehr als 100 Punkte hat.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Zum Schluss, wir die Variable "game" aus dem gegebenen Spielraum auf "false" gesetzt und die Methode
     * endRound() wird aufgerufen.
     * @param gameRoomName der Spielraumnamen.
     */
    void endGame(String gameRoomName) throws RemoteException;

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
    void startGame(String gameRoomName) throws RemoteException;

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
    boolean isMaxPlayersReached(String gameRoomName) throws RemoteException, NotAllPlayersJoinedException;

    /**
     * Diese Methode setzt den Spieler, der zuerst beigetreten ist als Dealer.
     * Die Methode nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    void setDealer(String gameRoomName) throws RemoteException;

    /**
     * Diese Methode entzieht dem gegebenen Spieler den Dealer Status.
     * Als Parameter wird die spezifische Benutzerkennung bzw. die UserID erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wurde.
     */
    void undoDealer(String userID) throws RemoteException;

    /**
     * Diese Methode setzt den Gewinner Status, für jeden Spieler in dem gegebenen Spielraum, zurück.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * @param gameRoomName der Spielraumname.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    void resetWinner(String gameRoomName) throws RemoteException;


    /**
     * Diese Methode entfernt einen Spieler aus einem Spielraum.
     * Es nimmt den Spielraumnamen als Parameter. Dieser wird dann verwendet, um, mithilfe des
     * gameRoomService Objektes, das Spielraumobjekt zu bekommen.
     * Zudem wird die spezifische Benutzerkennung bzw. die UserID als Parameter erwartet.
     * @param gameRoomName der Spielraumname.
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    void removePlayerOutOfGameRoom(String gameRoomName, String userID) throws RemoteException;

    /**
     * Diese Methode setzt alle Attribute des Spieler objekts zurück.
     * Es wird die spezifische Benutzerkennung bzw. die UserID als Parameter erwartet.
     * Diese wird dann in das RegistrationServiceobjekt übergeben, damit wird das Spieler objekt bekommen.
     * @param userID die Benutzerkennung des Spielers.
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    void resetPlayer(String userID) throws RemoteException;

    /**
     * Überprüft, ob ein Spieler aus dem Spiel rausgeflogen ist.
     * @param userID die Benutzerkennung des gegebenen Spielers.
     * @param gameRoomName der Spielraumname worin sich der Spieler gerade befindet.
     * @return "true", falls dies der Fall sein sollte, ansonsten "false".
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    boolean isPlayerOut(String userID, String gameRoomName) throws RemoteException;

}
