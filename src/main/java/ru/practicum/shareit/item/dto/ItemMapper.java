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

    public static ItemForRequest getItemForRequest(Item item) {
        return ItemForRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }

    public static ItemDto mapToDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .name(item.getName())
                .available(item.isAvailable())
                .build();
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
        return itemDto;
    }



}
