package com.nsu.midpointmassiveoperations.exception;

public class MidpointDoesntResponseException extends RuntimeException {
    public MidpointDoesntResponseException(String message) {
        super(message);
    }

    public MidpointDoesntResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
