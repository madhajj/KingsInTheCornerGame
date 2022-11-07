package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die auftritt solange es noch freie Plätze im Spielraum gibt.
 */

public class NotAllPlayersJoinedException extends Exception{
    /**
     * {@inheritDoc}
     */
    public NotAllPlayersJoinedException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public NotAllPlayersJoinedException(String message, Throwable cause) {
        super(message, cause);
    }
}
