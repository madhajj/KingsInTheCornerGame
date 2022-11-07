package com.sep.kitc.adapter.storage.entity;

import com.sep.kitc.common.exception.IllegalMoveException;
import org.junit.Test;

import static org.junit.Assert.*;

public class StallTest {

    @Test
    public void isPlayable() throws IllegalMoveException {
        Stall stall = new Stall();
        Card card1 = new Card("Black", "Spades", 13);
        Card card2 = new Card("Black", "Spades", 6);

        stall.setKingsStall(false);
        assertTrue(stall.isPlayable(card2));

        stall.setKingsStall(true);

        assertTrue(stall.isPlayable(card1));
        assertFalse(stall.isPlayable(card2));


        stall.addCardToStall(card1);
        assertFalse(stall.isPlayable(card2));
    }

    @Test
    public void addCardtoStall() throws IllegalMoveException {
        Stall stall = new Stall();
        Card card1 = new Card("Red","Hearts", 6);
        Card card2 = new Card("Black","Spades", 5);
        Card card3 = new Card("Black", "Spades", 4);

        stall.setKingsStall(false);

        stall.addCardToStall(card1);
        assertEquals(card1, stall.getTopCard());
        assertEquals(card1, stall.getBottomCard());

        stall.addCardToStall(card2);
        assertEquals(card1, stall.getBottomCard());
        assertEquals(card2, stall.getTopCard());

        try{
            stall.addCardToStall(card3);
        } catch (IllegalMoveException e) {
            assertEquals(card1, stall.getBottomCard());
            assertEquals(card2, stall.getTopCard());
        }
    }

    @Test
    public void isEmpty() throws IllegalMoveException {
        Stall stall = new Stall();
        assertTrue(stall.isEmpty());

        stall.addCardToStall(new Card("Red", "Hearts", 3));
        assertFalse(stall.isEmpty());

    }

    @Test
    public void isMergeable() throws IllegalMoveException {
        Stall stall1 = new Stall();
        Stall stall2 = new Stall();
        Card card1 = new Card("Black", "Spades", 13);
        Card card2 = new Card("Red", "Hearts", 12);
        Card card3 = new Card("Black", "Spades", 11);
        Card card4 = new Card("Red", "Diamonds", 10);

        stall1.addCardToStall(card1);
        stall1.addCardToStall(card2);

        stall2.addCardToStall(card3);
        stall2.addCardToStall(card4);

        assertTrue(stall2.isMergeable(stall1));

    }

    @Test
    public void mergeStalls() throws IllegalMoveException {
        Stall stall1 = new Stall();
        Stall stall2 = new Stall();
        Card card1 = new Card("Black", "Spades", 13);
        Card card2 = new Card("Red", "Hearts", 12);
        Card card3 = new Card("Black", "Spades", 11);
        Card card4 = new Card("Red", "Diamonds", 10);

        stall1.addCardToStall(card1);
        stall1.addCardToStall(card2);
        System.out.println("stall1");

        stall2.addCardToStall(card3);
        stall2.addCardToStall(card4);
        System.out.println("stall2");

        stall2.mergeStalls(stall1);
        assertEquals(card4, stall1.getTopCard());
        assertEquals(card1, stall1.getBottomCard());
        assertNull(stall2.getTopCard());
        assertNull(stall2.getBottomCard());
    }

    @Test
    public void isKingStall() {
    }
}