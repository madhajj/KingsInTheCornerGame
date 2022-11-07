package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die auftritt, sobald ein Spieler, mit der gleichen Spieler kennzeichnung,
 * die das System für diesen Benutzer erstellt hat, versucht mehrfach einzuloggen.
 */


public class PlayerHasAlreadyLoggedInException extends Exception{

    /**
     * {@inheritDoc}
     */
    public PlayerHasAlreadyLoggedInException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public PlayerHasAlreadyLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
}
