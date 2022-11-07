package com.sep.kitc.adapter.storage.entity;


import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void isKing() {
        Card card1  = new Card("Black", "Spades", 7);
        assertFalse(card1.isKing());
        Card card2  = new Card("Red", "Hearts", 13);
        assertTrue(card2.isKing());
    }

    @Test
    public void testToString() {
        Card card1  = new Card("Black", "Spades", 7);
        assertEquals(card1.toString(), "7 of Spades");
        Card card2  = new Card("Red", "Hearts", 13);
        assertEquals(card2.toString(), "King of Hearts");
        Card card3  = new Card("Red", "Diamonds", 1);
        assertEquals(card3.toString(), "Ace of Diamonds");
        Card card4  = new Card("Red", "Diamonds", 12);
        assertEquals(card4.toString(), "Queen of Diamonds");
        Card card5  = new Card("Black", "Clubs", 11);
        assertEquals(card5.toString(), "Jack of Clubs");
        assertNotEquals("10 of Diamonds", card1.toString());
        assertNotEquals("10 of Spades", card2.toString());
    }
}