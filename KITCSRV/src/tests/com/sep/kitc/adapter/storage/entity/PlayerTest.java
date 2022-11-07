package com.sep.kitc.adapter.storage.entity;

import com.sep.kitc.common.exception.IllegalMoveException;
import com.sep.kitc.common.exception.NotThePlayersTurnException;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void addCardToHand() {
        Player player = new Player ("Ilie", "123");
        Card card = new Card("Red", "Diamonds", 10);

        player.addCardToHand(card);
        assertNotNull(player.getHand());
        assertFalse(player.getHand().contains(new Card ("Black", "Spades", 10)));
        assertTrue(player.getHand().contains(card));

    }

    @Test
    public void addChips() {
        Player player = new Player ("Ilie", "123");

        player.addChips(40);

        assertTrue(player.hasChips());
        assertNotEquals(52, player.getChips());
        assertEquals(40, player.getChips());

    }


    @Test
    public void playCard() throws NotThePlayersTurnException, IllegalMoveException, RemoteException {
        Card card1 = new Card("Red", "Diamonds", 10);
        Card card2 = new Card("Black", "Clubs", 13);
        Card card3 = new Card("Black", "Spades", 2);
        Stall stall = new Stall();

        Player player = new Player ("Ilie", "123");
        player.addCardToHand(card1);
        player.addCardToHand(card2);
        player.addCardToHand(card3);
        player.setPlayersTurn(true);

        player.playCard(card1, stall);
        assertFalse(player.getHand().contains(card1));

        try{
            player.playCard(card2, stall);
        } catch (IllegalMoveException e) {
            assertTrue(player.getHand().contains(card2));
        }

        player.setPlayersTurn(false);
        try{
            player.playCard(card3, stall);
        } catch (NotThePlayersTurnException e){
            assertTrue(player.getHand().contains(card3));
        }

    }

    @Test
    public void payChips() {
        Player player = new Player ("Ilie", "123");
        player.addChips(80);
        int chipsToPay = 10;
        player.payChips(chipsToPay);
        assertEquals(player.getChips(), 70);
    }

    @Test
    public void hasKing(){
        Player player = new Player ("Ilie", "123");
        Card card1 = new Card("Red", "Diamonds", 10);
        Card card2 = new Card("Black", "Clubs", 13);
        Card card3 = new Card("Black", "Spades", 2);
        player.addCardToHand(card1);
        player.addCardToHand(card2);
        player.addCardToHand(card3);

        assertTrue(player.hasKing());
        Player player2 = new Player ("Ilie", "123");

        player2.addCardToHand(card1);
        player2.addCardToHand(card3);
        assertFalse(player2.hasKing());

    }
}