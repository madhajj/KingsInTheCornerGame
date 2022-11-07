package com.sep.kitc.common;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ChatService extends Remote, Serializable {

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
    void sendMessage(String gameRoomName, String userID, String message) throws RemoteException;

    /**
     * Gibt die, im Chat vorhandenen Nachrichten aus einem bestimmten Spielraum, als eine ArrayListe zurück.
     * Diese ArrayListe kann dann dem Client übergeben werden, damit es auch dargestellt werden kann.
     *
     * @param gameRoomName Der Spielraumname worin die Chatnachrichten stehen.
     * @return Die ArrayListe mit den Chatnachrichten
     * @throws RemoteException             falls die Verbindung zum Server unterbrochen wurde.
     */
    ArrayList<String> getMessages(String gameRoomName) throws RemoteException;
}
