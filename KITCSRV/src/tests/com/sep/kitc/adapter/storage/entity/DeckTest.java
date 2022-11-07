package com.sep.kitc.adapter.storage.entity;

import com.sep.kitc.common.exception.NoMoreCardsInDeckException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DeckTest {

    @Test
    public void createDeck() {
        Deck deck = new Deck();
        assertEquals(deck.getNumberOfCards(), 52);
    }

    @Test
    public void addCardToDeck() throws NoMoreCardsInDeckException {
        Deck deck = new Deck();
        Card card1 = deck.drawTopCard();
        Card card2 = deck.drawTopCard();
        Card card3 = deck.drawTopCard();
        Card card4 = deck.drawTopCard();
        assertEquals(deck.getNumberOfCards(), 48);
        deck.addCardToDeck(card1);
        assertEquals(card1, deck.getTopCard());
        deck.addCardToDeck(card2);
        assertEquals(card2, deck.getTopCard());
        deck.addCardToDeck(card3);
        assertEquals(card3, deck.getTopCard());
        deck.addCardToDeck(card4);
        assertEquals(card4, deck.getTopCard());
        assertEquals(deck.getNumberOfCards(), 52);
    }

    @Test
    public void getNumberOfCards() throws NoMoreCardsInDeckException {
        Deck deck = new Deck();
        assertEquals(deck.getNumberOfCards(), 52);
        deck.drawTopCard();
        deck.drawTopCard();
        deck.drawTopCard();
        deck.drawTopCard();
        assertEquals(deck.getNumberOfCards(), 48);
        assertNotEquals(deck.getNumberOfCards(), 52);
        Card card = deck.drawTopCard();
        assertTrue(card.getValue() < 14);
        assertTrue(card.getValue() > 0 );
    }

    @Test
    public void drawTopCard() throws NoMoreCardsInDeckException {
        Deck deck = new Deck();
        assertEquals(deck.getNumberOfCards(), 52);
        deck.drawTopCard();
        deck.drawTopCard();
        deck.drawTopCard();
        deck.drawTopCard();
        assertEquals(deck.getNumberOfCards(), 48);
        assertNotEquals(deck.getNumberOfCards(), 52);
        Card card = deck.drawTopCard();
        assertTrue(card.getValue() < 14);
        assertTrue(card.getValue() > 0 );
        ArrayList<String> suits = new ArrayList<>();
        suits.add("Diamonds");
        suits.add("Hearts");
        suits.add("Spades");
        suits.add("Clubs");
        assertTrue(suits.contains(card.getSuit()));
        for(int i = 1; i < 48; i++){
            deck.drawTopCard();
        }

        boolean b;
        try{
            deck.drawTopCard();
        }catch(NoMoreCardsInDeckException e){
            b = true;
            assertTrue(b);
        }
    }
}