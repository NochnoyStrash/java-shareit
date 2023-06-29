package ru.practicum.shareit.booking.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAuthor;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {
    BookingMapper bookingMapper;
    EasyRandom generator = new EasyRandom();

    @Test
    public void getBookingAuthorTest() {
        BookingDtoAuthor bookingDtoAuthor = generator.nextObject(BookingDtoAuthor.class);
        Booking booking = BookingMapper.getBookingAuthor(bookingDtoAuthor);
        assertEquals(booking.getItem(), bookingDtoAuthor.getItem());
        assertEquals(booking.getStart(), bookingDtoAuthor.getStart());
        assertEquals(booking.getStatus(), bookingDtoAuthor.getStatus());
        assertEquals(booking.getEnd(), bookingDtoAuthor.getEnd());
    }

    @Test
    public void getBookingTest() {
        BookingDtoCreate bookingDtoCreate = generator.nextObject(BookingDtoCreate.class);
        Item item = generator.nextObject(Item.class);
        Booking booking = BookingMapper.getBooking(bookingDtoCreate, item);
        assertEquals(booking.getItem(), item);
        assertEquals(booking.getEnd(), bookingDtoCreate.getEnd());
        assertEquals(booking.getStart(), bookingDtoCreate.getStart());
    }

    @Test
    public void getBookingDtoTest() {
        Booking booking = generator.nextObject(Booking.class);
        BookingDto bookingDto = new BookingDto();
        bookingDto = BookingMapper.getBookingDto(booking);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getBooker(), bookingDto.getBooker());
        assertEquals(booking.getItem(), bookingDto.getItem());
    }

    @Test
    public void getBookingDtoAuthorTest() {
        Booking booking = generator.nextObject(Booking.class);
        BookingDtoAuthor bookingDtoAuthor = BookingMapper.getBookingDtoAuthor(booking);
        assertEquals(booking.getId(), bookingDtoAuthor.getId());
        assertEquals(booking.getItem(), bookingDtoAuthor.getItem());
        assertEquals(booking.getEnd(), bookingDtoAuthor.getEnd());
        assertEquals(booking.getStart(), bookingDtoAuthor.getStart());
        assertEquals(booking.getStatus(), bookingDtoAuthor.getStatus());
    }

}