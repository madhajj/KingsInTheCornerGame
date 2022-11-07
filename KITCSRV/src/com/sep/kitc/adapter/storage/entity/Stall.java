package com.sep.kitc.adapter.storage.entity;

import com.sep.kitc.adapter.storage.service.RegistrationService;
import com.sep.kitc.common.exception.IllegalMoveException;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stall implements Serializable {

    private static Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());
    private Queue<Card> queue;
    private Card topCard;
    private  Card bottomCard;
    private boolean kingStall;

    public Stall(){
        kingStall = false;
        queue = new LinkedList<>();
    }

    /**
     * Diese Methode prüft, ob man die gegebene Karte spielen kann.
     * @param card die Karte die spielen möchte.
     * @return "true", falls der Zug gültig ist, ansonsten "false".
     */
    public boolean isPlayable(Card card){
        if(isKingStall()){
            if(queue.isEmpty() && card.isKing()){
                return true;
            }else if(!queue.isEmpty()){
                return (card.getColor() != getTopCard().getColor() && card.getValue() == getTopCard().getValue()-1);
            }else{
                return false;
            }
        }else{
            if(queue.isEmpty()){
                return true;
            }
            return (card.getColor() != getTopCard().getColor() && card.getValue() == getTopCard().getValue()-1);
        }

    }

    /**
     * Fügt dem Stapel eine Karte hinzu, wenn diese gespielt werden kann.
     * @param card die Karte die man spielen möchte.
     * @throws IllegalMoveException falls es sich um einen ungültigen Zug handelt.
     */
    public void addCardToStall(Card card) throws IllegalMoveException {
        if(isPlayable(card)){
            if(queue.isEmpty()){
                setBottomCard(card);
            }
            setTopCard(card);
            queue.add(card);
        } else {
            LOGGER.log(Level.SEVERE, "Diese Karte kann man nicht spielen");
            throw new IllegalMoveException("Diese Karte kann man nicht spielen");
        }

    }

    public Card getBottomCard(){
        return bottomCard;
    }

    public Card getTopCard(){
        return topCard;
    }

    public void setBottomCard(Card biggerCard){
        bottomCard = biggerCard;
    }

    public void setTopCard(Card smallerCard){
        topCard = smallerCard;
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    /**
     * Diese Methode überprüft, ob man zwei Stapel stapeln kann.
     * Als Parameter erwartet es den Stapel wo man den momentanen Stapel drauflegen will.
     * Laut den Regeln von KingsInTheCorner darf man keine Königsstapel bewegen.
     * Die unterste Karte, des zu bewegenden Stapels, muss kleiner sein und die Farbe darf nicht gleiche sein
     * als auf dem Stapel wo man es drauflegen will.
     * @param toMergeStall der Stapel wo man den momentanen Stapel drauflegen will.
     * @return "true", falls der Zug gültig ist, ansonsten "false".
     */
    public boolean isMergeable(Stall toMergeStall){
        if(toMergeStall.isKingStall()){
            if(toMergeStall.isEmpty()) {
                if (getBottomCard().isKing()) {
                    return true;
                } else {
                    return false;
                }
            }else{
                return (getBottomCard().getColor() != toMergeStall.getTopCard().getColor() && getBottomCard().getValue() == toMergeStall.getTopCard().getValue()-1);
            }
        }else{
            if (toMergeStall.isEmpty()){
                return true;
            }else{
                return (getBottomCard().getColor() != toMergeStall.getTopCard().getColor() && getBottomCard().getValue() == toMergeStall.getTopCard().getValue()-1);
            }
        }
    }

    /**
     * Diese Methode fügt zwei Stapel zusammen bzw. stapelt zwei Stapel.
     * Mit der isMergeable() Methode wird überprüft, ob es sich um einen gültigen Zug handelt.
     * Falls dies der Fall ist, dann werden die Stapel zusammen gefügt.
     * @param stall der Stapel worauf man den momentanen Stapel drauflegen möchte.
     * @throws IllegalMoveException falls es sich um einen ungültigen Zug handelt.
     */
    public void mergeStalls(Stall stall) throws IllegalMoveException{
        if(isMergeable(stall)){
            while (queue.size() > 0){
                stall.addCardToStall(queue.poll());
            }
            setTopCard(null);
            setBottomCard(null);
            queue.clear();
        } else {
            LOGGER.log(Level.SEVERE, "Man kann diese Stalls nicht stapeln");
            throw new IllegalMoveException("Man kann diese Stalls nicht stapeln");
        }
    }

    public boolean isKingStall(){
        return kingStall;
    }

    public void setKingsStall(boolean pKingStall){
        kingStall = pKingStall;
    }
}
