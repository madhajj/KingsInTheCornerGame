package com.sep.kitc.common.exception;

public class RoomAlreadyExistsException extends Exception{
    public RoomAlreadyExistsException(String message) {
        super(message);
    }
    public RoomAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
