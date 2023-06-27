package ru.practicum.shareit.item.dto;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingInfo;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    EasyRandom generator = new EasyRandom();

    @Test
    void getItemDtoLastNextTest() {
        Item item = generator.nextObject(Item.class);
        BookingInfo bookingInfoLast = generator.nextObject(BookingInfo.class);
        BookingInfo bookingInfoNext = generator.nextObject(BookingInfo.class);

        ItemDtoLastAndNextBooking i = ItemMapper.getItemDtoLastNext(item, bookingInfoLast, bookingInfoNext);
        assertEquals(item.getId(), i.getId());
        assertEquals(item.getName(), i.getName());
        assertEquals(bookingInfoNext, i.getNextBooking());
        assertEquals(bookingInfoLast, i.getLastBooking());
    }

    @Test
    void getItemForRequestTest() {
        Item item = generator.nextObject(Item.class);
        ItemForRequest i = ItemMapper.getItemForRequest(item);
        assertEquals(item.getId(), i.getId());
        assertEquals(item.getOwner().getId(), i.getOwnerId());
        assertEquals(item.getName(), i.getName());
    }

    @Test
    void mapToDtoTest() {
        Item item = generator.nextObject(Item.class);
        ItemDto itemDto = ItemMapper.mapToDto(item);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.isAvailable(), itemDto.getAvailable());
        assertEquals(item.getRequest().getId(), itemDto.getRequestId());

    }
}