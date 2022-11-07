package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die beim Beitreten eines Spielraumes auftritt.
 */

public class GameRoomNotCreatedException extends Exception{
    /**
     * {@inheritDoc}
     */
    public GameRoomNotCreatedException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public GameRoomNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
