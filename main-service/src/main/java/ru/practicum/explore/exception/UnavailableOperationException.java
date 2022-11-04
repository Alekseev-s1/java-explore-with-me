package ru.practicum.explore.exception;

public class UnavailableOperationException extends RuntimeException {
    public UnavailableOperationException(String message) {
        super(message);
    }
}
