package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die auftritt sobald ein Deck keine Karten mehr beinhaltet.
 */

public class NoMoreCardsInDeckException extends Exception{
    /**
     * {@inheritDoc}
     */
    public NoMoreCardsInDeckException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public NoMoreCardsInDeckException(String message, Throwable cause) {
        super(message, cause);
    }
}
