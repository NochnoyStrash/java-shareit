package ru.practicum.shareit.item.dto;


import ru.practicum.shareit.booking.dto.BookingInfo;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDtoLastAndNextBooking getItemDtoLastNext(Item item, BookingInfo last, BookingInfo next) {
        return ItemDtoLastAndNextBooking.builder()
                .id(item.getId())
                .description(item.getDescription())
                .name(item.getName())
                .available(item.isAvailable())
                .nextBooking(next)
                .lastBooking(last)
                .build();
    }



}
