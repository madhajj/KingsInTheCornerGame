package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die auftritt, sobald ein Spieler, versucht das Konto
 * eines anderen Spielers zu löschen, während der andere Spieler noch angemeldet ist.
 */
public class PlayerCannotBeDeletedException extends Exception{

    /**
     * {@inheritDoc}
     */
    public PlayerCannotBeDeletedException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public PlayerCannotBeDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
