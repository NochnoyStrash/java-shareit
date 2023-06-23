package ru.practicum.shareit.request.exception;

public class NotFoundRequestException extends RuntimeException {
    public NotFoundRequestException(String massage) {
        super(massage);
    }
}
