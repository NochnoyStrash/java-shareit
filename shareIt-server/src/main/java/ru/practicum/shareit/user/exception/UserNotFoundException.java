package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.generrat.Generated;

@Generated
public class UserNotFoundException extends RuntimeException {

    public  UserNotFoundException(String massage) {
        super(massage);
    }
}
