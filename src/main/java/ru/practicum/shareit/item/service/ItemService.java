package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLastAndNextBooking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    Item updateItem(ItemDto itemDto, long userId, long itemId);

    Item getItem(Long itemId);

    ItemDto getItemDto(long itemId);

    List<ItemDto> getMasterItemsDto(long userId);

    List<ItemDto> findItemFromText(String text);

    List<ItemDtoLastAndNextBooking> findItemWithBookingLastNext(Long ownerId);

    ItemDtoLastAndNextBooking getItemLastNext(Long itemId, Long ownerId);

    Comment addComment(Long itemId, Long userId, CommentDto commentDto);

    CommentDto getComment(Comment comment);
}
