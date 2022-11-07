package com.sep.kitc.adapter.storage.service;

import com.sep.kitc.adapter.storage.entity.GameRoom;
import com.sep.kitc.common.exception.CreateGameRoomException;
import com.sep.kitc.common.exception.GameRoomNotCreatedException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateGameRoomServiceTest {

    @Test
    public void nameAlreadyExists() throws CreateGameRoomException, GameRoomNotCreatedException {
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        for (int i = 0; i < 20; i++){
            GameRoom gameRoom = new GameRoom("gm" + i, "123" + i, 4);
            createGameRoomService.createGameRoom(gameRoom);
        }
        for (int i = 0; i < 20; i++){
            assertTrue(createGameRoomService.nameAlreadyExists("gm" + i));
            assertFalse(createGameRoomService.nameAlreadyExists("gm" + i + 13));
        }
    }

    @Test
    public void createGameRoom() throws CreateGameRoomException, GameRoomNotCreatedException {
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        for (int i = 0; i < 20; i++){
            GameRoom gameRoom = new GameRoom("gm" + i, "123" + i, 4);
            createGameRoomService.createGameRoom(gameRoom);
            GameRoom gm = createGameRoomService.getGameRoom("gm" + i);
            assertEquals(gameRoom, gm);
        }

        for (int i = 0; i < 20; i++){
            GameRoom gameRoom = new GameRoom("gm" + i, "123" + i, 4);
            boolean b = false;
            try{
                createGameRoomService.createGameRoom(gameRoom);
            }catch(CreateGameRoomException e){
                b = true;
            }
            if(b) assertTrue(true);
            else assertTrue(false);

        }
    }

    @Test
    public void playerHasAlreadyJoined() throws CreateGameRoomException, GameRoomNotCreatedException {
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        for(int i = 1 ; i < 10; i++) {
            GameRoom gameRoom = new GameRoom("gm" + i, "123" + i, 4);
            gameRoom.addPlayer("s0lonari56" + i);
            createGameRoomService.createGameRoom(gameRoom);
            assertTrue(createGameRoomService.playerHasAlreadyJoined("gm" + i, "s0lonari56" + i));
        }
    }

    @Test
    public void getGameRoom() throws CreateGameRoomException, GameRoomNotCreatedException {
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        for(int i = 1 ; i < 10; i++) {
            GameRoom gameRoom = new GameRoom("gm" + i, "123" + i, 4);
            createGameRoomService.createGameRoom(gameRoom);
            assertEquals(gameRoom, createGameRoomService.getGameRoom("gm" + i));
        }
        for(int i = 20 ; i < 45; i++) {
            boolean b = false;
            try {
                createGameRoomService.getGameRoom("gm" + i * 23);
            }catch (GameRoomNotCreatedException e){
                b = true;
            }
            if(b) assertTrue(true);
            else assertTrue(false);
        }
    }

    @Test
    public void getCreateGameRoomService() throws CreateGameRoomException, GameRoomNotCreatedException {
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        for(int i = 1 ; i < 10; i++) {
            GameRoom gameRoom = new GameRoom("gm" + i, "123" + i, 4);
            createGameRoomService.createGameRoom(gameRoom);
            assertTrue(createGameRoomService.getCreateGameRoomService().contains(gameRoom));
        }
    }
}