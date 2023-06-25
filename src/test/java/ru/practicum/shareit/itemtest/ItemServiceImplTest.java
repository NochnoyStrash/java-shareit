package ru.practicum.shareit.itemtest;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLastAndNextBooking;
import ru.practicum.shareit.item.exception.CommentsValidateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemsRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    ItemServiceImpl itemService;
    @Mock
    ItemsRepository mockItemsRepository;
    @Mock
    BookingRepository mockBookingRepository;
    @Mock
    CommentRepository mockCommentRepository;
    @Mock
    RequestRepository mockRequestRepository;

    @Mock
    UserService mockUserService;
    EasyRandom generator = new EasyRandom();
    User user;
    Item item;


    @BeforeEach
    public void beforeEach() {
        itemService = new ItemServiceImpl(mockItemsRepository, mockUserService,
                mockBookingRepository, mockCommentRepository, mockRequestRepository);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");

        user = new User(1L, "Вася", "enot@mail.ru");
        item = new Item(1L, "Дрель", "Мощная дрель", user, true, null);
    }

    @Test
    public void addItemTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");
        itemDto.setAvailable(true);


        Mockito
                .when(mockUserService.getUser(Mockito.anyLong()))
                .thenReturn(user);

        Mockito
                .when(mockItemsRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        ItemDto itemDto1 = itemService.addItem(itemDto, 1);
        Assertions.assertEquals(itemDto1.getId(), item.getId(), "ID  не равны");
    }

    @Test
    public void updateItemTest() {
        Mockito
                .when(mockItemsRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");
        itemDto.setAvailable(true);

        final ItemNotFoundException e = assertThrows(ItemNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                itemService.updateItem(itemDto, 2, 1);
            }
        });
        assertEquals("У пользователя с ID = 2  нет вещей с ID = 1", e.getMessage());

    }

    @Test
    public void updateItemTestWithException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");
        itemDto.setAvailable(true);
        Mockito
                .when(mockItemsRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        final ItemNotFoundException e = assertThrows(ItemNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                itemService.updateItem(itemDto, 1, 1);
            }
        });
        assertEquals("Не найдена вещь с таким ID = 1", e.getMessage());
    }

    @Test
    public void getItemDtoTest() {
        Mockito
                .when(mockItemsRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        ItemDto itemDto = itemService.getItemDto(1);
        assertEquals(itemDto.getId(), item.getId(), "id не равны");
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getAvailable(), item.isAvailable());
    }

    @Test
    public void getLastNext() {
        Booking booking = new Booking(null, LocalDateTime.now().plusHours(12),
                LocalDateTime.now().plusDays(1), item, user, StatusBooking.APPROVED);
        Booking booking1 = new Booking(null, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), item, user, StatusBooking.APPROVED);
        Comment comment = new Comment(1L, "jiuyuiol", item, user, LocalDateTime.now());
        Mockito
                .when(mockItemsRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(mockCommentRepository.findComments(Mockito.anyLong()))
                .thenReturn(List.of(comment));
        Mockito
                .when(mockBookingRepository.findLastBookingItem(Mockito.anyLong()))
                .thenReturn(List.of(booking1));
        Mockito
                .when(mockBookingRepository.findNextBookingItem(Mockito.anyLong()))
                .thenReturn(List.of(booking));

        ItemDtoLastAndNextBooking itemDtoLastAndNextBooking = itemService.getItemLastNext(1L, 1L);
        assertEquals(itemDtoLastAndNextBooking.getId(), item.getId());
        assertEquals(itemDtoLastAndNextBooking.getLastBooking().getId(), booking1.getId());
        assertEquals(itemDtoLastAndNextBooking.getNextBooking().getId(), booking.getId());
        assertEquals(itemDtoLastAndNextBooking.getComments().get(0).getText(), comment.getText());
    }

    @Test
    public void findItemFromTextTest() {


        Mockito
                .when(mockItemsRepository.searchText(" ", PageRequest.of(1, 1)))
                .thenReturn(new PageImpl<>(List.of()));

        List<ItemDto> itemDtos = itemService.findItemFromText(" ", 1, 1);

        Mockito
                .when(mockItemsRepository.searchText("дрел", PageRequest.of(1, 1)))
                .thenReturn(new PageImpl<>(List.of(item)));
        List<ItemDto> itemDtos1 = itemService.findItemFromText("дрел", 1, 1);
        assertEquals(itemDtos.size(), 0);
        assertEquals(itemDtos1.size(), 1);
        assertEquals(itemDtos1.get(0).getId(), item.getId());
    }

    @Test
    public void addCommentTest() {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorName("dfcz");
        commentDto.setText("sdggs");
        commentDto.setId(1L);
        List<Booking> bookings = new ArrayList<>();
        Mockito
                .when(mockBookingRepository.findPastBookingByUser(1L, 1L))
                .thenReturn(bookings);
        Mockito
                .when(mockCommentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(new Comment());

        CommentsValidateException e = assertThrows(CommentsValidateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                itemService.addComment(1L, 1L, commentDto);
            }
        });
        assertEquals("Комментарии могут оставлять только пользователи вещи с заверщенным заказом", e.getMessage());

        Booking booking = generator.nextObject(Booking.class);
        bookings.add(booking);
        Comment comment = itemService.addComment(1L, 1L, commentDto);
        Mockito.verify(mockCommentRepository, Mockito.times(1))
                .save(Mockito.any(Comment.class));
    }

    @Test
    public void  getMasterItemsDtoTest() {
        Item item = generator.nextObject(Item.class);
        List<Item> items = List.of(item);
        Mockito
                .when(mockItemsRepository.findAllByOwnerId(Mockito.anyLong()))
                .thenReturn(items);
        List<ItemDto> itemDtos = itemService.getMasterItemsDto(1L);
        assertEquals(itemDtos.get(0).getId(), item.getId());

    }


}
