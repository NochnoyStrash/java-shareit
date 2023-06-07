package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAuthor;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.ValidateBookingAndItemxception;
import ru.practicum.shareit.booking.exception.ValidateBookingException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final ItemService itemService;
    private final BookingRepository bookingRepository;
    private final UserService userService;


    @Transactional
    public Booking createBooking(BookingDtoCreate bookingDto, Long userId) {
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (!(start.isAfter(LocalDateTime.now()) && end.isAfter(LocalDateTime.now()) && start.isBefore(end))) {
            throw new ValidateBookingException("Неправильная дата заказа");
        }
        Item item = itemService.getItem(bookingDto.getItemId());
        if (item.getOwner().getId().equals(userId)) {
            throw new UserNotFoundException("Владелец вещи не может быть и заказчиком");
        }
        if (!item.isAvailable()) {
            throw new ValidateBookingException("Вещь недоситупна для заказа");
        }
        User booker = userService.getUser(userId);
        Booking booking = BookingMapper.getBooking(bookingDto, item);
        booking.setStatus(StatusBooking.WAITING);
        booking.setBooker(booker);
        return bookingRepository.save(booking);
    }

    @Transactional
    public BookingDto createBookingDto(Booking booking) {
        return BookingMapper.getBookingDto(booking);
    }

    public BookingDtoAuthor createBookingDtoAuthor(Booking booking) {
        return BookingMapper.getBookingDtoAuthor(booking);
    }

    @Transactional
    public BookingDtoCreate createBookingDtoCreate(Booking booking) {
        return BookingMapper.getBookingDtoCreate(booking);
    }

    public Booking findBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(
                        String.format("Такого запроса на бронирования с ID = %d не надено", bookingId)));
    }

    @Transactional
    public BookingDtoAuthor confirmBooking(Long bookingId, Long ownerId, boolean approved) {
        Booking booking = findBooking(bookingId);
        Long ownerId2 = booking.getItem().getOwner().getId();
        if (!Objects.equals(ownerId2, ownerId)) {
            throw new UserNotFoundException("Подтверидить заказ может только владелец вещи");
        }
        if (!booking.getStatus().equals(StatusBooking.WAITING)) {
            throw  new ValidateBookingException("Нелья поменять уже подтвержденной вещи");
        }
        if (approved) {
            booking.setStatus(StatusBooking.APPROVED);
        } else {
            booking.setStatus(StatusBooking.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapper.getBookingDtoAuthor(booking);
    }

    public BookingDtoAuthor findBookingForAuthor(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findBookingForAuthor(bookingId, userId).orElseThrow(
                () -> new ValidateBookingAndItemxception(String.format("Не правильный userID = %d или bookingID = %d", userId, bookingId)));
        return BookingMapper.getBookingDtoAuthor(booking);
    }

    public List<Booking> findAllBookingByUser(Long userId, String state) {
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByUser(userId);
                return bookings;
            case "CURRENT":
                bookings = bookingRepository.findAllByUserCurrent(userId);
                return bookings;
            case "PAST":
                bookings = bookingRepository.findAllByUserPost(userId);
                return bookings;
            case "FUTURE":
                bookings = bookingRepository.findAllByUserFuture(userId);
                return bookings;
            case "WAITING":
                bookings = bookingRepository.findAllByUserWaiting(userId);
                return bookings;
            case "REJECTED":
                bookings = bookingRepository.findAllByUserRejected(userId);
                return bookings;
            default:
                throw new ValidateBookingException(String.format("Unknown state: %S", state));
        }
    }

    public List<Booking> findAllBookingByOwner(Long userId, String state) {
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByOwner(userId);
                return bookings;
            case "CURRENT":
                bookings = bookingRepository.findAllByOwnerCurrent(userId);
                return bookings;
            case "PAST":
                bookings = bookingRepository.findAllByOwnerPost(userId);
                return bookings;
            case "FUTURE":
                bookings = bookingRepository.findAllByOwnerFuture(userId);
                return bookings;
            case "WAITING":
                bookings = bookingRepository.findAllByOwnerWaiting(userId);
                return bookings;
            case "REJECTED":
                bookings = bookingRepository.findAllByOwnerRejected(userId);
                return bookings;
            default:
                throw new ValidateBookingException(String.format("Unknown state: %S", state));
        }
    }

    public List<BookingDtoAuthor> findAllBookingDtoByUser(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("Заказы не найдены");
        }
        return bookings.stream().map(BookingMapper::getBookingDtoAuthor).collect(Collectors.toList());
    }

}
