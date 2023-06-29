package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;
    private static final String userIdHeaders = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(userIdHeaders) Long userId,
                                             @RequestBody @Valid ItemRequestDto request) {
        return requestClient.addRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsFromUser(@RequestHeader(userIdHeaders) Long userId) {
        return requestClient.getRequestsFromUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequest(@RequestHeader(userIdHeaders) Long userId,
                                              @RequestParam(defaultValue = "0") Integer from, @RequestParam(defaultValue = "20") Integer size) {
        return requestClient.getAllRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(userIdHeaders) Long userId, @PathVariable Long requestId) {
        return requestClient.getRequest(userId, requestId);
    }
}
