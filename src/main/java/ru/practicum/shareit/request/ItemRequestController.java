package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestService requestService;

    private static final String userIdHeaders = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader(userIdHeaders) Long userId, @RequestBody @Valid ItemRequestDto request) {
        ItemRequest itemRequest = requestService.addRequest(request, userId);
        return requestService.getRequestDto(itemRequest);
    }

    @GetMapping
    public List<ItemRequestDto> getRequestsFromUser(@RequestHeader(userIdHeaders) Long userId) {
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        List<ItemRequest> itemRequests = requestService.getRequestFromUser(userId);
        itemRequests.forEach(i -> itemRequestDtos.add(requestService.getRequestDto(i)));
        return itemRequestDtos;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequest(@RequestHeader(userIdHeaders) Long userId,
                                              @RequestParam(defaultValue = "0") Integer from, @RequestParam(defaultValue = "20") Integer size) {
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        Page<ItemRequest> allRequests = requestService.getAllRequests(userId, from, size);
        allRequests.forEach(i -> itemRequestDtos.add(requestService.getRequestDto(i)));
        return itemRequestDtos;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader(userIdHeaders) Long userId, @PathVariable Long requestId) {
        ItemRequest itemRequest = requestService.getItemRequest(requestId, userId);
        return requestService.getRequestDto(itemRequest);
    }
}
