package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLastAndNextBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String userIdHeaders = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ItemDtoLastAndNextBooking getItem(@RequestHeader(userIdHeaders) long userId, @PathVariable long itemId) {
        return itemService.getItemLastNext(itemId, userId);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(userIdHeaders) long userId, @RequestBody @Valid ItemDto itemDto) {
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item patchItem(@RequestHeader(userIdHeaders) long userId, @RequestBody ItemDto itemDto, @PathVariable long itemId) {
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping
    public List<ItemDtoLastAndNextBooking> getItemMaster(@RequestHeader(userIdHeaders) long userId) {
        return itemService.findItemWithBookingLastNext(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.findItemFromText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(userIdHeaders) long userId,
                                       @PathVariable long itemId, @RequestBody @Valid CommentDto commentDto) {
        Comment comment = itemService.addComment(itemId, userId, commentDto);
        return itemService.getComment(comment);

    }
}
