package com.sep.kitc.common;

import com.sep.kitc.common.exception.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface GameRoomService extends Remote, Serializable {
    /**
     * Erstellt einen Spielraum dem man beitreten kann. Spielräume die erstellt wurden, werden im System eingetragen.
     * Spielräume müssen nur einmal erstellt werden.
     *
     * @param gameRoomName     Name des Spielraumes der erstellt wird.
     * @param gameRoomPassword Passwort des Spielraumes der erstellt wird.
     * @param maxPlayers       Maximale Anzahl an Spielern die diesem Spielraum beitreten können
     *                         Ein ganze Zahl zwischen 1 und 7 wird erwartet.
     * @throws RemoteException         falls die Verbindung an dem Server unterbrochen wird.
     * @throws CreateGameRoomException falls ein Spielraum mit diesem Namen bereits existiert.
     */
    void createGameRoom(String gameRoomName, String gameRoomPassword, int maxPlayers) throws RemoteException, CreateGameRoomException;

    /**
     * Behandelt den Spielbeitritt für einen bestehenden Benutzer
     *
     * @param gameRoomName     Name des Spielraumes dem man beitreten möchte
     * @param gameRoomPassword Passwort des Spielraumes dem man beitreten möchte
     * @param userIdent
     * @throws RemoteException       falls die Verbindung an dem Server unterbrochen wird.
     * @throws GameRoomNotCreatedException falls der Spielraum noch nicht erstellt worden ist.
     * @throws PasswordIncorrectException falls das Passwort falsch eingegeben wurde.
     */
    void joinGameRoom(String gameRoomName, String gameRoomPassword, String userIdent) throws RemoteException, PasswordIncorrectException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, MaxPlayersReachedException;

    /**
     * Überprüft ob der gegebene Spielraumname schon in unserem System vorhanden ist. Es kann immer nur einen Spielraum mit
     * einem spezifischen Namen geben.
     * @param roomName Spielraumname
     * @return True, falls es diesen geben sollte und False falls dies nicht der Fall ist.
     * @throws RemoteException falls die Verbindung vom Server unterbrochen wird.
     * @throws RoomAlreadyExistsException falls es bereits einen Spielraum mit diesem Namen geben sollte.
     */
    boolean nameAlreadyExists(String roomName) throws RemoteException, RoomAlreadyExistsException;

    /**
     * Gibt eine Hashmap, mit den Spielraumnamen und die Anzahl an Spielern die sich gerade in diesem Spielraum befinden, zurück.
     * @return Hashmap mit Spielraumnamen und Anzahl an Spielern in diesem Spielraum
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    HashMap<String, Integer> getGameRoomMap() throws RemoteException;

    /**
     * Gibt eine Hashmap, mit den Spielraumnamen und die maximale Anzahl an Spielern in diesem Spielraum, zurück.
     * @return Hashmap mit Spielraumnamen und maximale Anzahl an Spielern in diesem Spielraum
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    HashMap<String, Integer> getGameRoomMapWithMaxPlayers() throws RemoteException;

}
