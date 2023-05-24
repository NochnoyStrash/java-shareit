package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public ItemDto getItem(@RequestHeader(userIdHeaders) long userId, @PathVariable long itemId) {
        return itemService.getItemDto(itemId, userId);
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
    public List<ItemDto> getItemMaster(@RequestHeader(userIdHeaders) long userId) {
        return itemService.getMasterItemsDto(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.findItemFromText(text);
    }
}
