package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die auftritt, sobald ein Spieler, mit der gleichen Spielerkennzeichnung,
 * die das System für diesen Benutzer erstellt hat, versucht mehrfach einem Spielraum beizutreten.
 */

public class PlayerHasAlreadyJoinedException extends Exception{

    /**
     * {@inheritDoc}
     */
    public PlayerHasAlreadyJoinedException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public PlayerHasAlreadyJoinedException(String message, Throwable cause) {
        super(message, cause);
    }
}
