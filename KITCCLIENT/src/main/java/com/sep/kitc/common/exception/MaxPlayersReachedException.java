package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die auftritt sobald ein Spielraum voll ist
 * und somit keine weiterer Spieler mehr beitreten kann.
 */

public class MaxPlayersReachedException extends Exception{
    /**
     * {@inheritDoc}
     */
    public MaxPlayersReachedException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public MaxPlayersReachedException(String message, Throwable cause) {
        super(message, cause);
    }
}
