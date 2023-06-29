package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

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
    public ItemRequestDto addRequest(@RequestHeader(userIdHeaders) Long userId, @RequestBody ItemRequestDto request) {
        ItemRequest itemRequest = requestService.addRequest(request, userId);
        return requestService.getRequestDto(itemRequest);
    }

    @GetMapping
    public List<ItemRequestDto> getRequestsFromUser(@RequestHeader(userIdHeaders) Long userId) {
        List<ItemRequest> itemRequests = requestService.getRequestFromUser(userId);
        return itemRequests.stream().map(requestService::getRequestDto).collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequest(@RequestHeader(userIdHeaders) Long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "20") Integer size) {
        Page<ItemRequest> allRequests = requestService.getAllRequests(userId, from, size);
        return allRequests.stream().map(requestService::getRequestDto).collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader(userIdHeaders) Long userId, @PathVariable Long requestId) {
        ItemRequest itemRequest = requestService.getItemRequest(requestId, userId);
        return requestService.getRequestDto(itemRequest);
    }
}
