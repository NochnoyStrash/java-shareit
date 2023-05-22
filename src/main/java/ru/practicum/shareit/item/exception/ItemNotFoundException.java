package ru.practicum.shareit.item.exception;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String massage) {
        super(massage);
    }
}
