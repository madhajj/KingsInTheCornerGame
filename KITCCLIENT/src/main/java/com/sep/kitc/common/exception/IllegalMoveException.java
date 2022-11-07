package com.sep.kitc.common.exception;


/**
 * Klasse zur Abbildung einer Ausnahme, die auftritt sobald man die Karte/den Stall nicht spielen/aufeinander legen kann
 */

public class IllegalMoveException extends Exception{
    /**
     * {@inheritDoc}
     */
    public IllegalMoveException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public IllegalMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
