package com.sep.kitc.adapter.storage.impl;

import com.sep.kitc.adapter.storage.service.ChatRoomService;
import com.sep.kitc.common.ChatService;
import com.sep.kitc.common.LoginService;
import com.sep.kitc.common.exception.GameRoomNotCreatedException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServiceImpl implements ChatService {

    private ChatRoomService chatRoomService;

    public ChatServiceImpl(){
        chatRoomService = new ChatRoomService();
    }


    private static Logger LOGGER = Logger.getLogger(LoginService.class.getName());

    /**
     * Sendet die gegebene Nachricht an den Chat in einem bestimmten Spielraum.
     * Diese Nachricht ist mit der Benutzerkennung markiert.
     * Mit dieser können wir dann leicht aus unserem Server den Namen des Benutzers rausfinden.
     * Die Nachrichten bestehen aus dem Benutzernamen und der versendeten Nachricht.
     *
     * @param gameRoomName Der Spielraumname in dessen Chat die Nachricht reingeschrieben wird.
     * @param userID       Die Benutzerkennung des Benutzers
     * @param message      Nachricht die gesendet wird.
     * @throws RemoteException             falls die Verbindung zum Server unterbrochen wurde.
     */
    @Override
    public synchronized void sendMessage(String gameRoomName, String userID, String message) throws RemoteException{
        try {
            chatRoomService.sendTheMessage(gameRoomName, userID, message);
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Gibt die, im Chat vorhandenen Nachrichten aus einem bestimmten Spielraum, als eine ArrayListe zurück.
     * Diese ArrayListe kann dann dem Client übergeben werden, damit es auch dargestellt werden kann.
     *
     * @param gameRoomName Der Spielraumname worin die Chatnachrichten stehen.
     * @return Die ArrayListe mit den Chatnachrichten
     * @throws RemoteException             falls die Verbindung zum Server unterbrochen wurde.
     */
    @Override
    public synchronized ArrayList<String> getMessages(String gameRoomName) throws RemoteException{
        try {
            return chatRoomService.getChatMessages(gameRoomName);
        } catch (GameRoomNotCreatedException e) {
            LOGGER.log(Level.SEVERE, "Spielraum mit diesem Spielraumnamen gibt es nicht!");
            throw new RuntimeException(e);
        }
    }
}
