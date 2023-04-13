package com.nsu.midpointmassiveoperations.exception;

public class JiraDoesntResponseException extends RuntimeException {
    public JiraDoesntResponseException(String message) {
        super(message);
    }

    public JiraDoesntResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
