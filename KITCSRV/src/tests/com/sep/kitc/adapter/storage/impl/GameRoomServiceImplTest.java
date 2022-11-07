package com.sep.kitc.adapter.storage.impl;

import com.sep.kitc.common.exception.*;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class GameRoomServiceImplTest {

    @Test
    public void createGameRoom() throws RemoteException, CreateGameRoomException {
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        for(int i = 0; i< 10; i++){
            gameRoomService.createGameRoom("gm" + i, "1234" + i, 5 );
            assertTrue(gameRoomService.getGameRoomMap().containsKey("gm" + i));
            assertTrue(gameRoomService.getGameRoomMapWithMaxPlayers().containsKey("gm" + i));
        }
    }

    @Test
    public void nameAlreadyExists() throws RemoteException, CreateGameRoomException, RoomAlreadyExistsException {
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        for(int i = 0; i< 10; i++){
            assertFalse(gameRoomService.nameAlreadyExists("gm" + i));
            gameRoomService.createGameRoom("gm" + i, "1234" + i, 5 );
        }
        for(int i = 9; i >= 0; i--){
            assertTrue(gameRoomService.nameAlreadyExists("gm" + i));
        }
    }

    @Test
    public void joinGameRoom() throws RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, PasswordIncorrectException {
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        for(int i = 0; i< 10; i++){
            gameRoomService.createGameRoom("gm" + i, "1234" + i, 3 );
            String userId = "solona7i" + i;
            gameRoomService.joinGameRoom("gm" + i, "1234" + i, userId);
            assertTrue(gameRoomService.getGameRoomMap().get("gm" + i).equals(1));
            boolean b = false;
            try{
                gameRoomService.joinGameRoom("gm" + i, "1234" + i, userId);
            }catch (PlayerHasAlreadyJoinedException e){
                b = true;
            }
            if(b) assertTrue(true);
            else assertTrue(false);
        }

        for(int i = 0; i< 10; i++){
            String userId1 = "tiganas88" + i;
            String userId2 = "jardan67" + i ;
            String userId3 = "cebotariV" + i * 17;
            gameRoomService.joinGameRoom("gm" + i, "1234" + i, userId1);
            gameRoomService.joinGameRoom("gm" + i, "1234" + i, userId2);
            boolean b = false;
            try{
                gameRoomService.joinGameRoom("gm" + i, "1234" + i, userId3);
            }catch (MaxPlayersReachedException e){
                b = true;
            }
            if(b) assertTrue(true);
            else assertTrue(false);
        }

        for(int i = 2; i< 12; i++){
            String userId = "tiganas88" + i;
            boolean b = false;
            try{
                gameRoomService.joinGameRoom("gm" + i*20, "1234" + i, userId);
            }catch (GameRoomNotCreatedException e){
                b = true;
            }
            if(b) assertTrue(true);
            else assertTrue(false);
        }


    }

}