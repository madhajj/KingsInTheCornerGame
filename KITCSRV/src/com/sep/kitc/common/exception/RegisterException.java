package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die bei der Registrierung auftritt.
 */
public class RegisterException extends Exception{

    /**
     * {@inheritDoc}
     */
    public RegisterException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
