package com.sep.kitc.adapter.storage.service;

import com.sep.kitc.adapter.storage.entity.GameRoom;
import com.sep.kitc.common.exception.CreateGameRoomException;
import com.sep.kitc.common.exception.GameRoomNotCreatedException;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateGameRoomService implements Serializable {

    private static Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());
    private static Set<GameRoom> createGameRoomService;
    public CreateGameRoomService(){
        createGameRoomService = new HashSet<>();
    }

    /**
     * Diese Methode prüft, ob der Name des Raums bereits in dem Set vorhanden ist.
     * @param roomName der Spielraumname
     * @return "true", falls es vorhanden ist, ansonsten "false".
     */
    public boolean nameAlreadyExists(String roomName){
        Optional<GameRoom> first = createGameRoomService.stream().filter(gameRoom1 -> gameRoom1.getGameName().equals(roomName)).findFirst();
        return first.isPresent();
    }

    /**
     * Diese Methode erstellt einen neuen Spielraum mit einem eindeutigen Namen.
     * @param gameRoom der Spielraumname.
     * @throws CreateGameRoomException falls es den Spielraum mit diesem Spielraumnamen schon geben sollte.
     */
    public void createGameRoom(GameRoom gameRoom) throws CreateGameRoomException{
        if (!nameAlreadyExists(gameRoom.getGameName())){
            createGameRoomService.add(gameRoom);
        } else {
            LOGGER.log(Level.SEVERE, "Spielraum wurde bereits erstellt");
            throw new CreateGameRoomException("Spielraum wurde bereits erstellt");
        }
    }

    /**
     * Diese Methode prüft, ob der Spieler dem Raum bereits beigetreten ist.
     * @param gameRoomName der Spielraumname
     * @param userIdent die Benutzerkennung eines Spielers.
     * @return "true", falls er vorhanden ist, ansonsten "false".
     * @throws GameRoomNotCreatedException falls einen Spielraum mit diesem Spielraumnamen nicht geben sollte.
     */
    public boolean playerHasAlreadyJoined(String gameRoomName, String userIdent) throws GameRoomNotCreatedException {
        return getGameRoom(gameRoomName).getPlayerSet().contains(userIdent);
    }

    /**
     * Findet einen Spielraum anhand seines Namens.
     * @param gameRoomName der Spielraumname
     * @return das Spielraumobjekt.
     * @throws GameRoomNotCreatedException  falls einen Spielraum mit diesem Spielraumnamen nicht geben sollte.
     */
    public GameRoom getGameRoom(String gameRoomName) throws GameRoomNotCreatedException{
        Optional<GameRoom> first = createGameRoomService.stream().filter(gameRoom1 -> gameRoom1.getGameName().equals(gameRoomName)).findFirst();
        if (first.isPresent()){
            return first.get();
        } else {
            LOGGER.log(Level.SEVERE, "Spielraum existiert nicht");
            throw new GameRoomNotCreatedException("Spielraum existiert nicht");
        }
    }

    public static Set<GameRoom> getCreateGameRoomService(){
        return createGameRoomService;
    }

}
