package ru.practicum.shareit.user.exception;

public class UserNotFoundException extends RuntimeException {

    public  UserNotFoundException(String massage) {
        super(massage);
    }
}