package ru.practicum.shareit.item.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLastAndNextBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceImplIntegrationTest {
    @Autowired
    ItemServiceImpl itemService;
    @Autowired
    UserService userService;
    @Autowired
    RequestService requestService;
    @Autowired
    BookingService bookingService;

    EasyRandom generator = new EasyRandom();
    User user;
    User user1;
    Booking booking;
    ItemRequest itemRequest;
    ItemRequest itemRequest1;
    ItemDto itemDto;

    @BeforeAll
    public void beforeAll() {
        user = new User(null, "Рома", "moro@mail.ru");
        user = userService.addUser(user);
        user1 = new User(null, "Нана", "haha@mail.ru");
        user1 = userService.addUser(user1);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .created(LocalDateTime.now())
                .description("нужна болгарка")
                .build();
        itemRequest = requestService.addRequest(itemRequestDto, user1.getId());
        ItemRequestDto itemRequestDto1 = ItemRequestDto.builder()
                .created(LocalDateTime.now())
                .description("нужен лобзик")
                .build();
        itemRequest1 = requestService.addRequest(itemRequestDto1, user.getId());
        itemDto = ItemDto.builder()
                .name("Болгарка")
                .description("большая болгарка")
                .available(true)
                .requestId(itemRequest.getId())
                .build();
        itemDto = itemService.addItem(itemDto, user.getId());
        BookingDtoCreate bookingDtoCreate = BookingDtoCreate.builder()
                .itemId(itemDto.getId())
                .start(LocalDateTime.now().plusHours(12))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        booking = bookingService.createBooking(bookingDtoCreate, user1.getId());
    }

    @Test
    public void findItemWithBookingLastNextTest() {
        List<ItemDtoLastAndNextBooking> list = itemService.findItemWithBookingLastNext(user.getId(), 1, 1);
        assertEquals(list.size(), 1);
        ItemDtoLastAndNextBooking item = list.get(0);
        assertEquals(item.getNextBooking().getId(), booking.getId());
        assertEquals(item.getDescription(), itemDto.getDescription());
    }

    @Test
    public void updateItemTest() {
        ItemDto itemDto1 = new ItemDto(1L, "Ворон", "торчвуд", false, itemRequest.getId());
        Item item3 = itemService.updateItem(itemDto1, user.getId(), itemDto.getId());
        assertEquals(item3.getName(), itemDto1.getName());
        assertEquals(item3.getDescription(), itemDto1.getDescription());
        assertEquals(item3.isAvailable(), itemDto1.getAvailable());
    }



}