package ru.practicum.shareit.booking.model;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAuthor;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.item.model.Item;

public class BookingMapper {

    private BookingMapper() {
    }

    public static Booking getBooking(BookingDtoCreate bookingDto, Item item) {
        return Booking.builder()
                .item(item)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();
    }

    public static Booking getBookingAuthor(BookingDtoAuthor bookingDtoAuthor) {
        return Booking.builder()
                .item(bookingDtoAuthor.getItem())
                .start(bookingDtoAuthor.getStart())
                .end(bookingDtoAuthor.getEnd())
                .status(bookingDtoAuthor.getStatus())
                .build();
    }

    public static BookingDto getBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .item(booking.getItem())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .build();
    }

    public static BookingDtoAuthor getBookingDtoAuthor(Booking booking) {
        return BookingDtoAuthor.builder()
                .id(booking.getId())
                .item(booking.getItem())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public static BookingDtoCreate getBookingDtoCreate(Booking booking) {
        return BookingDtoCreate.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .end(booking.getEnd())
                .start(booking.getStart())
                .build();
    }

}
