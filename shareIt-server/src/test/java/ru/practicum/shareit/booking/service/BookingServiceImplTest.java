package ru.practicum.shareit.booking.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAuthor;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.ValidateBookingAndItemxception;
import ru.practicum.shareit.booking.exception.ValidateBookingException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    BookingServiceImpl bookingService;
    @Mock
    ItemService itemService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserService userService;
    EasyRandom random = new EasyRandom();

    @BeforeEach
    public void beforeEach() {
        bookingService = new BookingServiceImpl(itemService,bookingRepository,userService);
    }

    @Test
    void createBookingTest() {
        User user = new User(1L, "Вася", "enot@mail.ru");
        User user1 = new User(2L, "Aся", "dot@mail.ru");
        Item item = new Item(1L, "Дрель", "Мощная дрель", user, true, null);
        BookingDtoCreate bookingDto = BookingDtoCreate.builder()
                .itemId(1L)
                .end(LocalDateTime.now().plusDays(2))
                .start(LocalDateTime.now().plusHours(12))
                .build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .status(StatusBooking.WAITING)
                .booker(user1)
                .build();


        Mockito
                .when(itemService.getItem(Mockito.anyLong()))
                .thenReturn(item);

        Mockito
                .when(userService.getUser(Mockito.anyLong()))
                .thenReturn(user1);
        Mockito
                .when(bookingRepository.save(Mockito.any(Booking.class)))
                        .thenReturn(booking);
        Booking booking1 = bookingService.createBooking(bookingDto, user1.getId());
        assertEquals(booking1.getItem(), booking.getItem());

        BookingDtoCreate bookingDto1 = BookingDtoCreate.builder()
                .itemId(1L)
                .end(LocalDateTime.now().plusDays(2))
                .start(LocalDateTime.now().minusHours(12))
                .build();
        final ValidateBookingException e = assertThrows(ValidateBookingException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Booking booking2 = bookingService.createBooking(bookingDto1, user1.getId());
            }
        });
        assertEquals("Неправильная дата заказа", e.getMessage());

        final UserNotFoundException ex = assertThrows(UserNotFoundException.class, () ->
                bookingService.createBooking(bookingDto, user.getId()));
        assertEquals("Владелец вещи не может быть и заказчиком", ex.getMessage());

    }


    @Test
    void createBookingDtoTest() {
        Booking booking = random.nextObject(Booking.class);
        BookingDto bookingDto = bookingService.createBookingDto(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getItem(), booking.getItem());
        assertEquals(bookingDto.getBooker(), booking.getBooker());
        assertEquals(bookingDto.getStart(), booking.getStart());

    }

    @Test
    void createBookingDtoAuthorTest() {
        Booking booking = random.nextObject(Booking.class);
        BookingDtoAuthor bookingDtoAuthor = bookingService.createBookingDtoAuthor(booking);
        assertEquals(bookingDtoAuthor.getId(), booking.getId());
        assertEquals(bookingDtoAuthor.getItem(), booking.getItem());
        assertEquals(bookingDtoAuthor.getBooker(), booking.getBooker());
        assertEquals(bookingDtoAuthor.getStart(), booking.getStart());
        assertEquals(bookingDtoAuthor.getStatus(), booking.getStatus());
    }

    @Test
    void createBookingDtoCreateTest() {
        Booking booking = random.nextObject(Booking.class);
        BookingDtoCreate bookingDtoCreate = bookingService.createBookingDtoCreate(booking);
        assertEquals(bookingDtoCreate.getId(), booking.getId());
        assertEquals(bookingDtoCreate.getItemId(), booking.getItem().getId());
        assertEquals(bookingDtoCreate.getStart(), booking.getStart());
        assertEquals(bookingDtoCreate.getEnd(), booking.getEnd());
    }

    @Test
    void findBookingTest() {

        Mockito
                .when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        final BookingNotFoundException e = assertThrows(BookingNotFoundException.class, this::execute7);
        assertEquals("Такого запроса на бронирования с ID = 1 не надено", e.getMessage());
    }

    public void execute7() throws Throwable {
        bookingService.findBooking(1L);
    }

    @Test
    void confirmBookingTest() {
        User user = new User(1L, "Вася", "enot@mail.ru");
        User user1 = new User(2L, "Aся", "dot@mail.ru");
        Item item = new Item(1L, "Дрель", "Мощная дрель", user, true, null);
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .status(StatusBooking.WAITING)
                .booker(user1)
                .build();

        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .status(StatusBooking.APPROVED)
                .booker(user1)
                .build();

        Mockito
                .when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking));
        Mockito
                .when(bookingRepository.save(booking))
                .thenReturn(booking1);

        BookingDtoAuthor bookingDtoAuthor = bookingService.confirmBooking(1L, 1L, true);
        assertEquals(bookingDtoAuthor.getId(), booking.getId());
        assertEquals(bookingDtoAuthor.getStatus(), StatusBooking.APPROVED);
        booking.setStatus(StatusBooking.WAITING);
        BookingDtoAuthor bookingDtoAuthor1 = bookingService.confirmBooking(1L, 1L, false);
        assertEquals(bookingDtoAuthor1.getStatus(), StatusBooking.REJECTED);

        UserNotFoundException e = assertThrows(UserNotFoundException.class, this::execute6);
        assertEquals("Подтверидить заказ может только владелец вещи", e.getMessage());

        booking.setStatus(StatusBooking.APPROVED);
        ValidateBookingException ex = assertThrows(ValidateBookingException.class, this::execute5);
        assertEquals("Нелья поменять уже подтвержденной вещи", ex.getMessage());


    }

    public void execute5() throws Throwable {
        BookingDtoAuthor bookingDtoAuthor = bookingService.confirmBooking(1L, 1L, true);
    }

    public void execute6() throws Throwable {
        BookingDtoAuthor bookingDtoAuthor = bookingService.confirmBooking(1L, 2L, true);
    }

    @Test
    public void findBookingForAuthorTestWithException() {
        Optional<Booking> bookings = Optional.empty();
        Booking booking = random.nextObject(Booking.class);
        Mockito
                .when(bookingRepository.findBookingForAuthor(1L, 1L))
                .thenReturn(bookings);
        ValidateBookingAndItemxception e = assertThrows(ValidateBookingAndItemxception.class, this::execute4);
        assertEquals("Не правильный userID = 1 или bookingID = 1", e.getMessage());

    }

    @Test
    public void findBookingForAuthorTest() {
        Booking booking = random.nextObject(Booking.class);
        Mockito
                .when(bookingRepository.findBookingForAuthor(1L, 1L))
                .thenReturn(Optional.of(booking));

        BookingDtoAuthor bookingDto = bookingService.findBookingForAuthor(1L, 1L);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
    }

    @Test
    void findAllBookingByUserTest() {
        Booking allBooking = random.nextObject(Booking.class);
        Booking currentBooking = random.nextObject(Booking.class);
        Booking postBooking = random.nextObject(Booking.class);
        Booking futureBooking = random.nextObject(Booking.class);
        Booking waitingBooking = random.nextObject(Booking.class);
        Booking rejectedBooking = random.nextObject(Booking.class);
        Mockito
                .when(bookingRepository.findAllByUser(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(allBooking)));
        Mockito
                .when(bookingRepository.findAllByUserCurrent(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(currentBooking)));
        Mockito
                .when(bookingRepository.findAllByUserPost(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(postBooking)));
        Mockito
                .when(bookingRepository.findAllByUserFuture(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(futureBooking)));
        Mockito
                .when(bookingRepository.findAllByUserWaiting(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(waitingBooking)));
        Mockito
                .when(bookingRepository.findAllByUserRejected(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(rejectedBooking)));
        Page<Booking> bookings = bookingService.findAllBookingByUser(1L,"ALL", 1, 1);
        assertEquals(bookings.stream().findFirst().get().getId(), allBooking.getId());

        Page<Booking> bookings1 = bookingService.findAllBookingByUser(1L,"CURRENT", 1, 1);
        assertEquals(bookings1.stream().findFirst().get().getId(), currentBooking.getId());

        Page<Booking> bookings2 = bookingService.findAllBookingByUser(1L,"PAST", 1, 1);
        assertEquals(bookings2.stream().findFirst().get().getId(), postBooking.getId());

        Page<Booking> bookings3 = bookingService.findAllBookingByUser(1L,"FUTURE", 1, 1);
        assertEquals(bookings3.stream().findFirst().get().getId(), futureBooking.getId());

        Page<Booking> bookings4 = bookingService.findAllBookingByUser(1L,"WAITING", 1, 1);
        assertEquals(bookings4.stream().findFirst().get().getId(), waitingBooking.getId());

        Page<Booking> bookings5 = bookingService.findAllBookingByUser(1L,"REJECTED", 1, 1);
        assertEquals(bookings5.stream().findFirst().get().getId(), rejectedBooking.getId());

        ValidateBookingException e = assertThrows(ValidateBookingException.class, this::execute3);
        assertEquals("Unknown state: SDFG", e.getMessage());
    }

    @Test
    void findAllBookingByOwnerTest() {
        Booking allBooking = random.nextObject(Booking.class);
        Booking currentBooking = random.nextObject(Booking.class);
        Booking postBooking = random.nextObject(Booking.class);
        Booking futureBooking = random.nextObject(Booking.class);
        Booking waitingBooking = random.nextObject(Booking.class);
        Booking rejectedBooking = random.nextObject(Booking.class);
        Mockito
                .when(bookingRepository.findAllByOwner(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(allBooking)));
        Mockito
                .when(bookingRepository.findAllByOwnerCurrent(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(currentBooking)));
        Mockito
                .when(bookingRepository.findAllByOwnerPost(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(postBooking)));
        Mockito
                .when(bookingRepository.findAllByOwnerFuture(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(futureBooking)));
        Mockito
                .when(bookingRepository.findAllByOwnerWaiting(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(waitingBooking)));
        Mockito
                .when(bookingRepository.findAllByOwnerRejected(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(rejectedBooking)));
        Page<Booking> bookings = bookingService.findAllBookingByOwner(1L,"ALL", 1, 1);
        assertEquals(bookings.stream().findFirst().get().getId(), allBooking.getId());

        Page<Booking> bookings1 = bookingService.findAllBookingByOwner(1L,"CURRENT", 1, 1);
        assertEquals(bookings1.stream().findFirst().get().getId(), currentBooking.getId());

        Page<Booking> bookings2 = bookingService.findAllBookingByOwner(1L,"PAST", 1, 1);
        assertEquals(bookings2.stream().findFirst().get().getId(), postBooking.getId());

        Page<Booking> bookings3 = bookingService.findAllBookingByOwner(1L,"FUTURE", 1, 1);
        assertEquals(bookings3.stream().findFirst().get().getId(), futureBooking.getId());

        Page<Booking> bookings4 = bookingService.findAllBookingByOwner(1L,"WAITING", 1, 1);
        assertEquals(bookings4.stream().findFirst().get().getId(), waitingBooking.getId());

        Page<Booking> bookings5 = bookingService.findAllBookingByOwner(1L,"REJECTED", 1, 1);
        assertEquals(bookings5.stream().findFirst().get().getId(), rejectedBooking.getId());

        ValidateBookingException e = assertThrows(ValidateBookingException.class, this::execute2);
        assertEquals("Unknown state: SDFG", e.getMessage());
    }

    @Test
    public void findAllBookingDtoByUserTest() {
        List<Booking> bookings = new ArrayList<>();
        BookingNotFoundException e = assertThrows(BookingNotFoundException.class, this::execute1);
        assertEquals("Заказы не найдены", e.getMessage());
        Booking booking = random.nextObject(Booking.class);
        bookings.add(booking);
        List<BookingDtoAuthor> bookingDtoAuthors = bookingService.findAllBookingDtoByUser(bookings);
        assertEquals(bookingDtoAuthors.get(0).getId(), booking.getId());
    }

    public void execute1() throws Throwable {
        List<Booking> bookings = new ArrayList<>();
        bookingService.findAllBookingDtoByUser(bookings);
    }

    public void execute2() throws Throwable {
        Page<Booking> booki = bookingService.findAllBookingByOwner(1L,"SDFG", 1, 1);
    }

    public void execute3() throws Throwable {
        Page<Booking> booki = bookingService.findAllBookingByUser(1L,"SDFG", 1, 1);
    }

    public void execute4() throws Throwable {
        BookingDtoAuthor bookingDto = bookingService.findBookingForAuthor(1L, 1L);
    }

}