package com.sep.kitc.adapter.storage.entity;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameRoomTest {

    @Test
    public void hasGameStarted() {
        GameRoom gameRoom = new GameRoom("gm1", "123", 4);
        assertFalse(gameRoom.hasGameStarted());
        gameRoom.startGame();
        assertTrue(gameRoom.hasGameStarted());
    }

    @Test
    public void hasRoundStarted() {
        GameRoom gameRoom = new GameRoom("gm1", "123", 4);
        assertFalse(gameRoom.hasRoundStarted());
        gameRoom.startGame();
        assertFalse(gameRoom.hasRoundStarted());
        gameRoom.startRound();
        assertTrue(gameRoom.hasRoundStarted());
    }

    @Test
    public void resetCards() {
        GameRoom gameRoom = new GameRoom("gm1", "123", 4);
        Card card = new Card("Black", "Spades", 7);
        gameRoom.getGameStalls().get("northStall").setBottomCard(card);
        assertEquals(gameRoom.getGameStalls().get("northStall").getBottomCard(), card);
        gameRoom.resetCards();
        assertNull(gameRoom.getGameStalls().get("northStall").getBottomCard());
    }


    @Test
    public void finishGame() {
        GameRoom gameRoom = new GameRoom("gm1", "123", 4);
        assertFalse(gameRoom.hasGameStarted());
        gameRoom.startGame();
        gameRoom.finishGame();
        assertFalse(gameRoom.hasGameStarted());
    }

    @Test
    public void finishRound(){
        GameRoom gameRoom = new GameRoom("gm1", "123", 4);
        gameRoom.startGame();
        gameRoom.startRound();
        assertTrue(gameRoom.hasRoundStarted());
        gameRoom.finishRound();
        assertFalse(gameRoom.hasRoundStarted());
    }

    @Test
    public void addPlayer(){
        GameRoom gameRoom = new GameRoom("gm1", "123", 4);
        gameRoom.addPlayer("asdfg140");
        gameRoom.addPlayer("qwerty789456");
        gameRoom.addPlayer("poiuy452");
        ArrayList<String> set = gameRoom.getPlayerSet();
        assertTrue(set.contains("asdfg140"));
        assertTrue(set.contains("qwerty789456"));
        assertTrue(set.contains("poiuy452"));

    }

    @Test
    public void removePlayer(){
        GameRoom gameRoom = new GameRoom("gm1", "123", 4);
        gameRoom.addPlayer("asdfg140");
        gameRoom.addPlayer("qwerty789456");
        gameRoom.addPlayer("poiuy452");
        ArrayList<String> set = gameRoom.getPlayerSet();
        assertEquals(3, set.size());
        gameRoom.removePlayer("qwerty789456");
        assertFalse(gameRoom.getPlayerSet().contains("qwerty789456"));
    }

    @Test
    public void equals(){
        GameRoom gameRoom1 = new GameRoom("gm1", "123", 4);
        GameRoom gameRoom2 = new GameRoom("gm1", "123", 4);
        GameRoom gameRoom3 = new GameRoom("gm2", "1447", 5);
        assertTrue(gameRoom1.equals(gameRoom2));
        assertFalse(gameRoom1.equals(gameRoom3));

    }
    @Test
    public void getters(){
        GameRoom gameRoom = new GameRoom("gm2", "1447", 5);
        assertEquals(gameRoom.getDeck().getNumberOfCards(), 52);
        assertEquals(gameRoom.getGameName(), "gm2");
        assertEquals(gameRoom.getGameStalls().size(), 8);
        assertEquals(gameRoom.getGamePassword(), "1447");
        assertEquals(gameRoom.getMaxPlayers(), 5);

    }
}