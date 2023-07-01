package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.exception.ValidateItemException;
import ru.practicum.shareit.exception.ValidateUserExcepion;
import ru.practicum.shareit.user.dto.UserDto;

public class Validation {

    private Validation() {

    }

    public static void  user(UserDto user) {
        if (user.getName() != null && user.getName().isBlank()) {
            throw new ValidateUserExcepion("Имя не может быть пустым");
        }
    }

    public static void item(ItemDto item) {
        if (item.getDescription() != null && item.getDescription().isBlank()) {
            throw new ValidateItemException("Описание не может быть пустым");
        }
        if (item.getName() != null && item.getName().isBlank()) {
            throw new ValidateItemException("Названи не может быть пустым");
        }
    }
}
