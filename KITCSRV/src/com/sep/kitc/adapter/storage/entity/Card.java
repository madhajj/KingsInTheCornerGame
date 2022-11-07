package com.sep.kitc.adapter.storage.entity;

import java.io.Serializable;
import java.util.Objects;


public class Card implements Serializable {

    private String color;
    private String suit;
    private int value;

    public Card(String pColor, String pSuit, int pValue) {
        color = pColor;
        suit = pSuit;
        value = pValue;
    }

    public String getColor(){
        return color;
    }

    public int getValue(){
        return value;
    }

    public String getSuit(){
        return suit;
    }

    /**
     * Überprüft, ob es sich bei dieser Karte um einen König handelt.
     * @return "true" falls König, ansonsten "false".
     */
    public boolean isKing() {
        // Wert 13 entspricht dem König.
        return (getValue() == 13);
    }

    /**
     * Gibt die Karte als String zurück.
     * Bei den Werten: 1,11,12,13 erhalten sie die entsprechenden Namen.
     * Beispiel: 4 of Hearts
     * @return Kartenname als String
     */
    public String toString() {
        String tempName;
        if(getValue() == 1){
            // Wert 1 entspricht dem As.
            tempName = "Ace";
        }else if(getValue() == 11){
            // Wert 11 entspricht dem Bube.
            tempName = "Jack";
        }else if(getValue() == 12){
            // Wert 12 entspricht der Dame.
            tempName = "Queen";
        }else if(getValue() == 13){
            // Wert 13 entspricht dem König.
            tempName = "King";
        }else{
            tempName = ""+getValue();
        }
        return tempName+" of "+getSuit();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return value == card.value && Objects.equals(color, card.color) && Objects.equals(suit, card.suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, suit, value);
    }
}
