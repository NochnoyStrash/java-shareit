package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Creating;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.Validation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String userIdHeaders = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(userIdHeaders) @NotNull Long userId, @PathVariable @NotNull Long itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(userIdHeaders) Long userId,
                                          @RequestBody @Validated(value = Creating.class) ItemDto itemDto) {
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader(userIdHeaders) @NotNull Long userId,
                                            @RequestBody  ItemDto itemDto,
                                            @PathVariable @NotNull Long itemId) {
        Validation.item(itemDto);
        return itemClient.patchItem(itemId, itemDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemMaster(@RequestHeader(userIdHeaders) @NotNull Long userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "20") @PositiveOrZero Integer size) {
        return itemClient.getItemMaster(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(userIdHeaders) Long userId,
                                             @RequestParam String text,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "20") @PositiveOrZero Integer size) {
        return itemClient.searchItem(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(userIdHeaders) @NotNull Long userId,
                                             @PathVariable @NotNull Long itemId, @RequestBody @Valid CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);

    }

}
