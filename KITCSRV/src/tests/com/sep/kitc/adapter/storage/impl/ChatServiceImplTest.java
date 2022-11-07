package com.sep.kitc.adapter.storage.impl;

import com.sep.kitc.adapter.storage.entity.GameRoom;
import com.sep.kitc.adapter.storage.entity.Player;
import com.sep.kitc.adapter.storage.service.ChatRoomService;
import com.sep.kitc.adapter.storage.service.RegistrationService;
import com.sep.kitc.common.exception.GameRoomNotCreatedException;
import com.sep.kitc.common.exception.RegisterException;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChatServiceImplTest {

    @Test
    public void sendMessage() throws GameRoomNotCreatedException, RegisterException, RemoteException {
        ChatRoomService crs = new ChatRoomService();
        ChatServiceImpl chatService = new ChatServiceImpl();
        RegistrationService registrationService = new RegistrationService();
        Player player1 = new Player("Andrei", "auebratve");
        GameRoom gm1 = new GameRoom("gm1", "123", 4);

        String p1Id =  registrationService.register(player1);

        crs.addGameRoom(gm1.getGameName());
        chatService.sendMessage(gm1.getGameName(), p1Id, "ma numesc Andrei");
        ArrayList<String> arr = new ArrayList<>();
        arr.add("Andrei: ma numesc Andrei");
        assertEquals(arr, crs.getChatMessages(gm1.getGameName()));

        chatService.sendMessage(gm1.getGameName(), "12", "loh");

        try {
            crs.sendTheMessage("gm2", p1Id, "wai");
        } catch (GameRoomNotCreatedException e) {
            assertTrue(true);
        }
    }

    @Test
    public void getMessages() throws RemoteException, GameRoomNotCreatedException {
        ChatRoomService crs = new ChatRoomService();
        ChatServiceImpl chatService = new ChatServiceImpl();
        GameRoom gm1 = new GameRoom("gm1", "123", 4);
        crs.addGameRoom(gm1.getGameName());
        assertEquals(new ArrayList<String>(), chatService.getMessages(gm1.getGameName()));

        try {
            chatService.getMessages("gm2");
        }catch (RemoteException e){
            assertTrue(true);
        }
    }
}