package ru.practicum.ewm.exceptions;

public class RequestAlreadyExistsException extends RuntimeException {
    public RequestAlreadyExistsException(String message) {
        super(message);
    }
}
