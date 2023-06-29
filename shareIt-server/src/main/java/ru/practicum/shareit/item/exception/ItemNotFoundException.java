package ru.practicum.shareit.item.exception;

import ru.practicum.shareit.generrat.Generated;

@Generated
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String massage) {
        super(massage);
    }
}
