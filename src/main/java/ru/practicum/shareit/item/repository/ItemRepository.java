package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> findItemByUserId(long userId);

    Item save(long userId, Item item);

    Item getItem(long userId, long itemId);

    void deleteItem(long userId, long itemId);

    List<Item> findFromText(String text);
}
