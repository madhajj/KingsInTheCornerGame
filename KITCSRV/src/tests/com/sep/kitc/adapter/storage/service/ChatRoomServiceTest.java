package com.sep.kitc.adapter.storage.service;

import com.sep.kitc.adapter.storage.entity.GameRoom;
import com.sep.kitc.adapter.storage.entity.Player;
import com.sep.kitc.adapter.storage.impl.ChatServiceImpl;
import com.sep.kitc.common.exception.GameRoomNotCreatedException;
import com.sep.kitc.common.exception.RegisterException;
import org.junit.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class ChatRoomServiceTest {

    @Test
    public void addGameRoom() throws GameRoomNotCreatedException {
        ChatRoomService crs = new ChatRoomService();
        GameRoom gm1 = new GameRoom("gm1", "123", 4);

        crs.addGameRoom(gm1.getGameName());
        assertEquals(new ArrayList<String>(), crs.getChatMessages(gm1.getGameName()));
    }

    @Test
    public void sendTheMessage() throws RegisterException, GameRoomNotCreatedException {
        ChatRoomService crs = new ChatRoomService();
        RegistrationService registrationService = new RegistrationService();
        Player player1 = new Player("Andrei", "auebratve");
        GameRoom gm1 = new GameRoom("gm1", "123", 4);

        String p1Id =  registrationService.register(player1);

        crs.addGameRoom(gm1.getGameName());
        crs.sendTheMessage(gm1.getGameName(), p1Id, "ma numesc Andrei");
        ArrayList<String> arr = new ArrayList<>();
        arr.add("Andrei: ma numesc Andrei");
        assertEquals(arr, crs.getChatMessages(gm1.getGameName()));

        crs.sendTheMessage(gm1.getGameName(), "12", "loh");

        try {
            crs.sendTheMessage("gm2", p1Id, "wai");
        } catch (GameRoomNotCreatedException e) {
            assertTrue(true);
        }

    }

    @Test
    public void getChatMessages() throws GameRoomNotCreatedException {
        ChatRoomService crs = new ChatRoomService();
        GameRoom gm1 = new GameRoom("gm1", "123", 4);
        crs.addGameRoom(gm1.getGameName());
        assertEquals(new ArrayList<String>(), crs.getChatMessages(gm1.getGameName()));

        try {
            crs.getChatMessages("gm2");
        }catch (GameRoomNotCreatedException e){
            assertTrue(true);
        }
    }
}