package ru.practicum.explore.exception;

public class UserIsNotParticipantException extends RuntimeException {
    public UserIsNotParticipantException(String message) {
        super(message);
    }
}
