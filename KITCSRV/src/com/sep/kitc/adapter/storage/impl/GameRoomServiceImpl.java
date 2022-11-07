package com.sep.kitc.adapter.storage.impl;

import com.sep.kitc.adapter.storage.service.ChatRoomService;
import com.sep.kitc.adapter.storage.service.CreateGameRoomService;
import com.sep.kitc.adapter.storage.entity.GameRoom;
import com.sep.kitc.common.GameRoomService;
import com.sep.kitc.common.LoginService;
import com.sep.kitc.common.exception.*;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameRoomServiceImpl implements GameRoomService {

    private static Logger LOGGER = Logger.getLogger(LoginService.class.getName());
    private CreateGameRoomService createGameRoomService;
    private static HashMap<String, Integer> gameRoomMap;
    private static HashMap<String, Integer> gameRoomMapWithMaxPlayers;

    private ChatRoomService chatRoomService;

    public GameRoomServiceImpl(){
        createGameRoomService = new CreateGameRoomService();
        gameRoomMap = new HashMap<>();
        gameRoomMapWithMaxPlayers = new HashMap<>();
        chatRoomService = new ChatRoomService();
    }


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
    @Override
    public synchronized void createGameRoom(String gameRoomName, String gameRoomPassword, int maxPlayers) throws RemoteException, CreateGameRoomException {
        createGameRoomService.createGameRoom(new GameRoom(gameRoomName, gameRoomPassword, maxPlayers));
        gameRoomMap.put(gameRoomName, 0);
        gameRoomMapWithMaxPlayers.put(gameRoomName, maxPlayers);
        chatRoomService.addGameRoom(gameRoomName);
    }

    /**
     * Überprüft ob der gegebene Spielraumname schon in unserem System vorhanden ist. Es kann immer nur einen Spielraum mit
     * einem spezifischen Namen geben.
     * @param roomName Spielraumname
     * @return True, falls es diesen geben sollte und False falls dies nicht der Fall ist.
     * @throws RemoteException falls die Verbindung vom Server unterbrochen wird.
     * @throws RoomAlreadyExistsException falls es bereits einen Spielraum mit diesem Namen geben sollte.
     */
    @Override
    public synchronized boolean nameAlreadyExists(String roomName) throws RemoteException, RoomAlreadyExistsException {
        return createGameRoomService.nameAlreadyExists(roomName);
    }

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
    @Override
    public synchronized void joinGameRoom(String gameRoomName, String gameRoomPassword, String userIdent) throws RemoteException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, MaxPlayersReachedException, PasswordIncorrectException {
        if (createGameRoomService.nameAlreadyExists(gameRoomName)){
            Optional<GameRoom> first = createGameRoomService.getCreateGameRoomService().stream().filter(gameRoom -> gameRoom.getGameName().equals(gameRoomName)).findFirst();
            GameRoom gameRoom = first.get();
            if (gameRoom.getPlayerSet().size() < gameRoom.getMaxPlayers()){
                if (!createGameRoomService.playerHasAlreadyJoined(gameRoomName, userIdent)){
                    if (gameRoom.getGamePassword().equals(gameRoomPassword)){
                        if (gameRoom.hasRoundStarted()){
                            gameRoom.addPlayerToWaitingList(userIdent);
                        } else {
                            gameRoom.addPlayer(userIdent);
                        }
                        gameRoomMap.replace(gameRoom.getGameName(), gameRoomMap.get(gameRoom.getGameName()) + 1);
                    } else {
                        throw new PasswordIncorrectException("Passwörter stimmen nicht überein");
                    }

                } else {
                    LOGGER.log(Level.SEVERE, "Spieler mit dieser Spielerkennzeichnung ist bereits beigetreten");
                    throw new PlayerHasAlreadyJoinedException("Spieler mit dieser Spielerkennzeichnung ist bereits beigetreten");
                }
            } else {
                LOGGER.log(Level.SEVERE, "Maximale anzahl an Spielern wurde erreicht");
                throw new MaxPlayersReachedException("Maximale anzahl an Spielern wurde erreicht");
            }
        } else {
            LOGGER.log(Level.SEVERE, "Spielraum mit gegebenem Spielraumnamen wurde noch nicht erstellt");
            throw new GameRoomNotCreatedException("Spielraum mit gegebenem Spielraumnamen wurde noch nicht erstellt");
        }
    }

    /**
     * Gibt eine Hashmap, mit den Spielraumnamen und die Anzahl an Spielern die sich gerade in diesem Spielraum befinden, zurück.
     * @return Hashmap mit Spielraumnamen und Anzahl an Spielern in diesem Spielraum
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    @Override
    public synchronized HashMap<String, Integer> getGameRoomMap() throws RemoteException {
        return gameRoomMap;
    }


    /**
     * Gibt eine Hashmap, mit den Spielraumnamen und die maximale Anzahl an Spielern in diesem Spielraum, zurück.
     * @return Hashmap mit Spielraumnamen und maximale Anzahl an Spielern in diesem Spielraum
     * @throws RemoteException falls die Verbindung zum Server unterbrochen wird.
     */
    @Override
    public synchronized HashMap<String, Integer> getGameRoomMapWithMaxPlayers() throws RemoteException{
        return gameRoomMapWithMaxPlayers;
    }
}
