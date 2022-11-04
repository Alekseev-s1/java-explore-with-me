package ru.practicum.explore.exception;

public class RequestsLimitException extends RuntimeException {
    public RequestsLimitException(String message) {
        super(message);
    }
}
