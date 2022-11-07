package com.sep.kitc.adapter.storage.entity;

import com.sep.kitc.common.exception.NoMoreCardsInDeckException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class Deck implements Serializable {

    private Stack<Card> deck = new Stack<>();

    /**
     * Beim Konstrukteur wird die createDeck() Methode aufgerufen.
     * Somit ist das Deck bei der Erstellung direkt funktionsfähig.
     */
    public Deck(){
        createDeck();
    }

    /**
     * Erstellt ein vollständiges Deck mit 52 Karten.
     * Außerdem wird es direkt gemischt.
     */
    public void createDeck() {
        ArrayList<Card> tempDeck = new ArrayList<>();
        String tempColor;
        String tempSuit;

        //Setzt die Werte der Karten
        for(int i = 1; i <= 13; i++){

            //Setzt den Symbol der Karten
            for(int j = 0; j < 4; j++){

                //Setzt die Farbe der Karten
                if(j % 2 == 0){
                    tempColor = "Red";
                }else{
                    tempColor = "Black";
                }
                if(j == 0){
                    // Herz == Hearts
                    tempSuit = "Hearts";
                }else if(j == 1){
                    // Pik == Spades
                    tempSuit = "Spades";
                }else if(j == 2){
                    // Karo == Diamonds
                    tempSuit = "Diamonds";
                }else{
                    // Kreuz == Clubs
                    tempSuit = "Clubs";
                }

                tempDeck.add(new Card(tempColor, tempSuit, i));
            }
        }


        // Fügt die Karten vom temporären Deck ungeordnet im Deck hinzu (mischen)
        while (tempDeck.size() > 0) {
            int index = (int) (Math.random() * tempDeck.size());
            deck.add(tempDeck.get(index));
            tempDeck.remove(index);
        }
    }

    /**
     * Fügt eine Karte dem Deck hinzu
     * @param card die Karte die man hinzufügen will.
     */
    public void addCardToDeck(Card card){
        deck.add(card);
    }

    /**
     * Gibt die Größe des Decks zurück.
     * @return Größe des Decks.
     */
    public int getNumberOfCards() {
        return deck.size();
    }

    /**
     * Überprüft, ob das Deck leer ist.
     * @return "true" falls leer, ansonsten "false".
     */
    public boolean isEmpty(){
        return deck.isEmpty();
    }

    /**
     * Gibt die oberste Karte im Stapel zurück.
     * @return die oberste Karte im Stapel.
     */
    public Card getTopCard(){
        return deck.peek();
    }

    /**
     * Zieht die oberste Karte vom Stapel.
     * @return die oberste Karte vom Stapel.
     * @throws NoMoreCardsInDeckException falls keine Karten mehr im Deck vorhanden sind.
     */
    public Card drawTopCard() throws NoMoreCardsInDeckException {
        if(deck.empty()){
            throw new NoMoreCardsInDeckException("Es befinden sich keine Karten mehr im Deck!");
        }
        return deck.pop();
    }
}
