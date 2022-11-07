package com.sep.kitc;

import com.sep.kitc.adapter.storage.impl.CardHandlerImpl;
import com.sep.kitc.adapter.storage.impl.ChatServiceImpl;
import com.sep.kitc.adapter.storage.impl.GameRoomServiceImpl;
import com.sep.kitc.adapter.storage.impl.LoginServiceImpl;
import com.sep.kitc.common.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    private  static Logger LOGGER = Logger.getLogger(Application.class.getName());
    private static LoginService loginService = null;
    private static GameRoomService gameRoomService = null;
    private static ChatService chatService = null;
    private static CardHandler cardHandler = null;

    /**
     * Startet den KITC Server-Prozess.
     * Der Server-Prozess geht davon aus, dass die RMI-Registry in einem gesonderten Prozess
     * gestartet wurde und der Server-Prozess sich so mit der RMI-Registry direkt verbinden kann.
     * Desweiteren wird davon ausgegangen, dass der verrwendete Port f&uuml-,r die RMI-Registry <b>1099</b>, also
     * der Standard-Port ist.
     */
    public static void main(String[] args) {
        Registry registry = null;

        try {
            //Instantiation-----------------------------------------------------------------------
            loginService = new LoginServiceImpl();
            gameRoomService = new GameRoomServiceImpl();
            chatService = new ChatServiceImpl();
            cardHandler = new CardHandlerImpl();
            LOGGER.log(Level.INFO, "Objekte erstellt");

            //Export Remote Object----------------------------------------------------------------
            LoginService loginStub = (LoginService) UnicastRemoteObject.exportObject(loginService, 0);
            GameRoomService gameRoomStub = (GameRoomService)  UnicastRemoteObject.exportObject(gameRoomService, 0);
            ChatService chatStub = (ChatService) UnicastRemoteObject.exportObject(chatService, 0);
            CardHandler cardHandlerStub = (CardHandler) UnicastRemoteObject.exportObject(cardHandler, 0);
            LOGGER.log(Level.INFO, "Versuche mit Registry zu verbinden");
            //registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));

            //Create Registry---------------------------------------------------------------------
            registry = LocateRegistry.createRegistry(1099);
            LOGGER.log(Level.INFO, "Registry enthalten, versuche zu binden");
            //BINDING-----------------------------------------------------------------------------
            registry.rebind("LoginService", loginStub);
            registry.rebind("GameRoomService", gameRoomStub);
            registry.rebind("ChatService", chatStub);
            registry.rebind("CardHandler", cardHandlerStub);
            LOGGER.log(Level.INFO, "Server ist mit rmi registry verbunden");
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, "Binding an RMI Registry fehlgeschlagen.", e);
            throw new RuntimeException(e);
        }    }
}
