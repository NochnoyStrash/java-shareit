package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoAuthor;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingService;
    private static final String userIdHeaders = "X-Sharer-User-Id";

    @PostMapping
    public BookingDtoAuthor createBooking(@RequestHeader(userIdHeaders) long userId, @RequestBody @Valid BookingDtoCreate bookingDto) {
        Booking booking = bookingService.createBooking(bookingDto, userId);
    return bookingService.createBookingDtoAuthor(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoAuthor confirmBooking(@RequestHeader(userIdHeaders) long userId, @PathVariable Long bookingId, @RequestParam boolean approved) {
        return bookingService.confirmBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoAuthor getBookingAuthor(@RequestHeader(userIdHeaders) long userId, @PathVariable Long bookingId) {
        return bookingService.findBookingForAuthor(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoAuthor> getAll(@RequestHeader(userIdHeaders) long userId, @RequestParam(defaultValue = "ALL") String state) {
        List<Booking> bookings = bookingService.findAllBookingByUser(userId, state);
        return bookingService.findAllBookingDtoByUser(bookings);
    }

    @GetMapping("/owner")
    public List<BookingDtoAuthor> getAllByOwner(@RequestHeader(userIdHeaders) long userId, @RequestParam(defaultValue = "ALL") String state) {
        List<Booking> bookings = bookingService.findAllBookingByOwner(userId, state);
        return bookingService.findAllBookingDtoByUser(bookings);
    }

}
