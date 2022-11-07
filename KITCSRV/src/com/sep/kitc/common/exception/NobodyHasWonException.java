package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die auftritt sobald man überprüft
 * ob ein Spieler in einem Spielraum eine Runde/Das Spiel gewonnen hat.
 * Ist kein so ein Spieler vorhanden, dann wird diese Exception geworfen.
 */

public class NobodyHasWonException extends Exception{
    /**
     * {@inheritDoc}
     */
    public NobodyHasWonException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public NobodyHasWonException(String message, Throwable cause) {
        super(message, cause);
    }
}
