package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemsRepository;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingRepositoryTest {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RequestRepository requestRepository;
    User user;
    User user1;
    User user2;
    Item item;
    Item item2;
    Booking booking;
    Booking booking1;
    Booking booking2;
    Booking booking3;

    @BeforeAll
    public void beforeAll() {
        user = new User(null, "Вася", "enot@mail.ru");
        usersRepository.save(user);
        user1 = new User(null, "Егор", "egor@mail.ru");
        usersRepository.save(user1);
        user2 = new User(null, "Енот", "raketa@mail.ru");
        usersRepository.save(user2);
        item = new Item(null, "Дрель", "Мощная дрель", user, true, null);
        item2 = new Item(null, "Отвертка", "трещеточная отвертка", user1, true, null);
        itemsRepository.save(item);
        itemsRepository.save(item2);
        booking = new Booking(null, LocalDateTime.now().plusHours(12),
                LocalDateTime.now().plusDays(1), item, user1, StatusBooking.WAITING);
        booking = bookingRepository.save(booking);
        booking1 = new Booking(null, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), item2, user, StatusBooking.APPROVED);
        booking1 = bookingRepository.save(booking1);
        booking2 = new Booking(null, LocalDateTime.now().minusHours(2),
                LocalDateTime.now().plusDays(1), item2, user, StatusBooking.APPROVED);
        booking2 = bookingRepository.save(booking2);
        booking3 = new Booking(null, LocalDateTime.now().plusHours(5),
                LocalDateTime.now().plusDays(2), item, user, StatusBooking.REJECTED);
        booking3 = bookingRepository.save(booking3);


    }

    @Test
    void findBookingForAuthor() {
        Booking bookings3 = bookingRepository.findBookingForAuthor(booking1.getId(), user.getId()).get();
        assertEquals(bookings3, booking1);
        final BookingNotFoundException e = assertThrows(BookingNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Booking bookings4 = bookingRepository.findBookingForAuthor(booking1.getId(), user2.getId())
                        .orElseThrow(() -> new BookingNotFoundException("запрос не найден"));
            }
        });
        assertEquals("запрос не найден", e.getMessage());


    }

    @Test
    void findAllByUser() {
        List<Booking> bookings = bookingRepository.findAllByUser(user.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 3, "размер списка заказов заказчика должен быть равен 3");

        List<Booking> bookings1 = bookingRepository.findAllByUser(user1.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings1.size(), 1, "размер списка заказов заказчика должен быть равен 1");

        List<Booking> bookings2 = bookingRepository.findAllByUser(user2.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings2.size(), 0, "размер списка заказов заказчика должен быть равен 0");

    }

    @Test
    void findAllByUserCurrent() {
        List<Booking> bookings = bookingRepository.findAllByUserCurrent(user.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 1, "размер списка заказов заказчика должен быть равен 1");

        List<Booking> bookings1 = bookingRepository.findAllByUserCurrent(user2.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings1.size(), 0, "размер списка заказов заказчика должен быть равен 0");
    }

    @Test
    void findAllByUserPost() {
        List<Booking> bookings = bookingRepository.findAllByUserPost(user.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 1, "размер списка заказов заказчика должен быть равен 1");

        List<Booking> bookings1 = bookingRepository.findAllByUserPost(user1.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings1.size(), 0, "размер списка заказов заказчика должен быть равен 0");

    }

    @Test
    void findAllByUserFuture() {
        List<Booking> bookings = bookingRepository.findAllByUserFuture(user1.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 1, "размер списка заказов заказчика должен быть равен 1");
    }

    @Test
    void findAllByUserWaiting() {
        List<Booking> bookings = bookingRepository.findAllByUserWaiting(user1.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 1, "размер списка заказов заказчика должен быть равен 1");

        List<Booking> bookings1 = bookingRepository.findAllByUserWaiting(user.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings1.size(), 0, "размер списка заказов заказчика должен быть равен 0");

    }

    @Test
    void findAllByUserRejected() {
        List<Booking> bookings = bookingRepository.findAllByUserRejected(user.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 1, "размер списка заказов заказчика должен быть равен 1");

        List<Booking> bookings1 = bookingRepository.findAllByUserRejected(user1.getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings1.size(), 0, "размер списка заказов заказчика должен быть равен 0");
    }

    @Test
    void findAllByOwnerCurrent() {
        List<Booking> bookings = bookingRepository.findAllByOwnerCurrent(item2.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 1, "размер списка заказов заказчика должен быть равен 1");

        List<Booking> bookings1 = bookingRepository.findAllByOwnerCurrent(item.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings1.size(), 0, "размер списка заказов заказчика должен быть равен 0");
    }

    @Test
    void findAllByOwnerPost() {
        List<Booking> bookings = bookingRepository.findAllByOwnerPost(item2.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 1, "размер списка заказов заказчика должен быть равен 1");
        assertTrue(bookings.get(0).getStart().isBefore(LocalDateTime.now()), "время начала заказа должно быть раньше текущего");

        List<Booking> bookings1 = bookingRepository.findAllByOwnerPost(item.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings1.size(), 0, "размер списка заказов заказчика должен быть равен 0");
    }

    @Test
    void testFindAllByOwnerFuture() {
        List<Booking> bookings = bookingRepository.findAllByOwnerFuture(item.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 2, "размер списка заказов заказчика должен быть равен 2");
        assertTrue(bookings.get(0).getStart().isAfter(LocalDateTime.now()), "время начала заказа должно быть позже текущего");
        List<Booking> bookings1 = bookingRepository.findAllByOwnerFuture(item2.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings1.size(), 0, "размер списка заказов заказчика должен быть равен 0");
    }

    @Test
    void findAllByOwnerWaiting() {
        List<Booking> bookings = bookingRepository.findAllByOwnerWaiting(item.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 1, "размер списка заказов заказчика должен быть равен 1");
        assertEquals(bookings.get(0).getStatus(), StatusBooking.WAITING, "статусы не равны");
        List<Booking> bookings1 = bookingRepository.findAllByOwnerWaiting(item2.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings1.size(), 0, "размер списка заказов заказчика должен быть равен 0");
    }

    @Test
    void findAllByOwnerRejected() {
        List<Booking> bookings = bookingRepository.findAllByOwnerRejected(item.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 1, "размер списка заказов заказчика должен быть равен 1");
        assertEquals(bookings.get(0).getStatus(), StatusBooking.REJECTED, "статусы не равны");

        List<Booking> bookings1 = bookingRepository.findAllByOwnerRejected(item2.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings1.size(), 0, "размер списка заказов заказчика должен быть равен 0");
    }

    @Test
    void findAllByOwner() {
        List<Booking> bookings = bookingRepository.findAllByOwner(item.getOwner().getId(), Pageable.unpaged()).getContent();
        assertEquals(bookings.size(), 2, "размер списка заказов заказчика должен быть равен 2");
        assertEquals(bookings.get(0).getItem().getOwner(), bookings.get(1).getItem().getOwner());
    }

}