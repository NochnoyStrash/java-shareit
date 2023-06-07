package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAuthor;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingService {
    Booking createBooking(BookingDtoCreate bookingDto, Long userId);

    BookingDto createBookingDto(Booking booking);

    BookingDtoCreate createBookingDtoCreate(Booking booking);

    Booking findBooking(Long bookingId);

    BookingDtoAuthor confirmBooking(Long bookingId, Long ownerId, boolean approved);

    BookingDtoAuthor findBookingForAuthor(Long bookingId, Long userId);


}
