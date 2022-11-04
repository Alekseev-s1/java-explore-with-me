package ru.practicum.explore.exception;

public class InitiatorIsOwnerException extends RuntimeException {
    public InitiatorIsOwnerException(String message) {
        super(message);
    }
}
