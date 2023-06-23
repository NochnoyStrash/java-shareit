package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {

    ItemRequest addRequest(ItemRequestDto requestDto, Long userId);

    ItemRequestDto getRequestDto(ItemRequest request);

    List<ItemRequest> getRequestFromUser(Long userId);

    Page<ItemRequest> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequest getItemRequest(Long requestId, Long userId);
}
