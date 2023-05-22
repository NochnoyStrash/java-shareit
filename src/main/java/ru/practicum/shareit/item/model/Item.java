package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    @EqualsAndHashCode.Include
    @PositiveOrZero
    private long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    private long userId;
    private boolean available = true;

    public Item(String name, String description, long userId, boolean available) {
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.available = available;
    }
}
