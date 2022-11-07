package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die auftritt sobald der Zug dem Spieler nicht gehöhrt.
 */

public class NotThePlayersTurnException extends Exception{
    /**
     * {@inheritDoc}
     */
    public NotThePlayersTurnException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public NotThePlayersTurnException(String message, Throwable cause) {
        super(message, cause);
    }
}
