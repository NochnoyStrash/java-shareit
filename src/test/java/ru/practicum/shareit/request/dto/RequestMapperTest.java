package ru.practicum.shareit.request.dto;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {
    EasyRandom generator = new EasyRandom();

    @Test
    void getItemRequestDto() {
        ItemRequest ir = generator.nextObject(ItemRequest.class);
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        ItemRequestDto itemRequestDto = RequestMapper.getItemRequestDto(ir, List.of(itemDto));
        assertEquals(ir.getId(), itemRequestDto.getId());
        assertEquals(ir.getDescription(), itemRequestDto.getDescription());
        assertEquals(ir.getCreated(), itemRequestDto.getCreated());
        assertEquals(itemDto, itemRequestDto.getItems().get(0));

    }

    @Test
    void getItemRequest() {
        User user = generator.nextObject(User.class);
        ItemRequestDto ir = generator.nextObject(ItemRequestDto.class);
        ir.setCreated(LocalDateTime.now());

        ItemRequest request = RequestMapper.getItemRequest(ir, user);
        assertEquals(request.getDescription(), ir.getDescription());
        assertEquals(request.getRequestor(), user);
    }
}