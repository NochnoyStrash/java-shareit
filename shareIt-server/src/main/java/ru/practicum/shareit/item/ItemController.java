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
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADERS = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ItemDtoLastAndNextBooking getItem(@RequestHeader(USER_ID_HEADERS) Long userId, @PathVariable Long itemId) {
        return itemService.getItemLastNext(itemId, userId);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_ID_HEADERS) Long userId, @RequestBody @Valid ItemDto itemDto) {
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item patchItem(@RequestHeader(USER_ID_HEADERS) Long userId, @RequestBody ItemDto itemDto, @PathVariable Long itemId) {
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping
    public List<ItemDtoLastAndNextBooking> getItemMaster(@RequestHeader(USER_ID_HEADERS) Long userId,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "20") @PositiveOrZero Integer size) {
        return itemService.findItemWithBookingLastNext(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader(USER_ID_HEADERS) Long userId,
                                    @RequestParam String text,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(defaultValue = "20") @PositiveOrZero Integer size) {
        return itemService.findItemFromText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_ID_HEADERS) Long userId,
                                 @PathVariable Long itemId, @RequestBody @Valid CommentDto commentDto) {
        Comment comment = itemService.addComment(itemId, userId, commentDto);
        return itemService.getComment(comment);

    }
}
