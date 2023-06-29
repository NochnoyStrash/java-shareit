package ru.practicum.shareit.exception;

public class ValidateItemException extends RuntimeException {
    public ValidateItemException(String massage) {
        super(massage);
    }
}
