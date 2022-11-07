package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die bei der Anmeldung auftritt wenn der Benutzer noch nicht registriert ist.
 */
public class UserNotCreatedException extends Exception{

    /**
     * {@inheritDoc}
     */
    public UserNotCreatedException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public UserNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
