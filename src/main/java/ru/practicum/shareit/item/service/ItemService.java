package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    Item updateItem(ItemDto itemDto, long userId, long itemId);

    ItemDto getItemDto(long itemId, long userId);

    List<ItemDto> getMasterItemsDto(long userId);

    List<ItemDto> findItemFromText(String text);
}
