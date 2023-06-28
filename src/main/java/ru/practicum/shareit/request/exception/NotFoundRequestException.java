package ru.practicum.shareit.request.exception;

import ru.practicum.shareit.generrat.Generated;

@Generated
public class NotFoundRequestException extends RuntimeException {
    public NotFoundRequestException(String massage) {
        super(massage);
    }
}
