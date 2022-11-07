package com.sep.kitc;

import com.sep.kitc.common.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dies ist die Klasse, die an den Server bindet,
 * sodass der Client eine Remoteverbindung zum Server herstellen kann
 */
public class RegistryHandler {
    private static Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static LoginService getLogInService(){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            LoginService loginService = (LoginService) registry.lookup("LoginService");
            LOGGER.log(Level.INFO, "Client hat sich das LoginService Objekt aus der Rmi registry geholt");
            return loginService;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Binding an RMI Registry fehlgeschlagen.", e);
            throw new RuntimeException(e);
        }
    }
    public static GameRoomService getGameRoomService(){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            GameRoomService gameRoomService = (GameRoomService) registry.lookup("GameRoomService");
            LOGGER.log(Level.INFO, "Client hat sich das GameRoomService Objekt aus der Rmi registry geholt");
            return gameRoomService;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Binding an RMI Registry fehlgeschlagen.", e);
            throw new RuntimeException(e);
        }
    }
    public static ChatService getChatService(){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ChatService chatService = (ChatService) registry.lookup("ChatService");
            LOGGER.log(Level.INFO, "Client hat sich das ChatService Objekt aus der Rmi registry geholt");
            return chatService;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Binding an RMI Registry fehlgeschlagen.", e);
            throw new RuntimeException(e);
        }
    }
    public static CardHandler getCardHandler(){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            CardHandler cardHandler = (CardHandler) registry.lookup("CardHandler");
            LOGGER.log(Level.INFO, "Client hat sich das ChatService Objekt aus der Rmi registry geholt");
            return cardHandler;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Binding an RMI Registry fehlgeschlagen.", e);
            throw new RuntimeException(e);
        }
    }
}
