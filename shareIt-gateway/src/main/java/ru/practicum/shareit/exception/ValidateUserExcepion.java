package ru.practicum.shareit.exception;

public class ValidateUserExcepion extends RuntimeException {
    public ValidateUserExcepion(String massage) {
        super(massage);
    }
}
