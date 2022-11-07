package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die bei der Erstellung eines Spielraumes auftritt.
 */

public class CreateGameRoomException extends Exception{

    /**
     * {@inheritDoc}
     */
    public CreateGameRoomException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public CreateGameRoomException(String message, Throwable cause) {
        super(message, cause);
    }

}
