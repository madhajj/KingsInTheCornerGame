package com.sep.kitc.adapter.storage.service;

import com.sep.kitc.common.exception.GameRoomNotCreatedException;
import com.sep.kitc.common.exception.UserNotCreatedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatRoomService implements Serializable {
    private static Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());

    /**
     * Diese HashMap speichert als Key den Spielraumnamen und als Value die Liste der Nachrichten als ArrayListe
     * von Strings.
     */
    private static HashMap<String, ArrayList<String>> messages;

    private RegistrationService registrationService = new RegistrationService();

    public ChatRoomService(){
        messages = new HashMap<>();
    }

    /**
     * Diese Methode fügt der Nachrichtenliste einen Spielraum hinzu,
     * sodass auf jeden Chat nur der entsprechende Raum zugreift
     * @param gameRoomName der Spielraumname.
     */
    public void addGameRoom(String gameRoomName){
        messages.put(gameRoomName, new ArrayList<String>());
    }

    /**
     * Diese Methode fügt die Nachricht dem entsprechenden Spielraum hinzu, die von
     * dem gegebenen Spieler geschrieben worden ist.
     * @param gameRoomName der Spielraumname
     * @param userID die Benutzerkennung des Spielers.
     * @param message die Nachricht die gesendet worden ist.
     * @throws GameRoomNotCreatedException falls es den Spielraum mit diesem Spielraumnamen nicht geben sollte.
     */
    public synchronized void sendTheMessage(String gameRoomName, String userID, String message) throws GameRoomNotCreatedException{
        if (messages.containsKey(gameRoomName)){
            try {
                String userName = registrationService.getUserNameOfPlayer(userID);
                ArrayList<String> chatRoomMessages = messages.get(gameRoomName);
                chatRoomMessages.add(userName + ": " + message);
                messages.replace(gameRoomName, chatRoomMessages);
            } catch (UserNotCreatedException e) {
                LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung gibt es nicht.");
            }
        } else {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new GameRoomNotCreatedException("Spielraum mit diesem Spielraumnamen gibt es nicht");
        }
    }


    /**
     * Diese Methode gibt uns eine ArrayListe mit allen Nachrichten aus dem Chat, von dem
     * gegebenen Spielraum, zurück.
     * @param gameRoomName der Spielraumname
     * @return ArrayListe mit den Nachrichten in diesem Spielraum, als String.
     * @throws GameRoomNotCreatedException falls es den Spielraum mit diesem Spielraumnamen nicht geben sollte.
     */
    public synchronized ArrayList<String> getChatMessages(String gameRoomName) throws GameRoomNotCreatedException {
        if (messages.containsKey(gameRoomName)){
            return messages.get(gameRoomName);
        } else {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht");
            throw new GameRoomNotCreatedException("Spielraum mit diesem Spielraumnamen gibt es nicht");
        }
    }
}
