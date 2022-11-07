package com.sep.kitc.common.exception;

/**
 * Klasse zur Abbildung einer Ausnahme, die auftritt, sobald man das Passwort falsch eingegeben hat.
 */

public class PasswordIncorrectException extends Exception{

    /**
     * {@inheritDoc}
     */
    public PasswordIncorrectException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public PasswordIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }
}
