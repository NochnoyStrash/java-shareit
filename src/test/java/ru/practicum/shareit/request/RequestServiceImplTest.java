package ru.practicum.shareit.request;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemsRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.NotFoundRequestException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    RequestServiceImpl requestService;
    @Mock
    RequestRepository requestRepository;
    @Mock
    ItemsRepository itemsRepository;
    @Mock
    UsersRepository usersRepository;
    ItemRequest itemRequest;
    User user;
    EasyRandom generator = new EasyRandom();
    Item item;

    @BeforeEach
    public void beforeEach() {
        requestService = new RequestServiceImpl(requestRepository, itemsRepository, usersRepository);
        user = generator.nextObject(User.class);
        itemRequest = new ItemRequest(1L, "Нужна отвертка-трещетка", user, LocalDateTime.now());
        item = generator.nextObject(Item.class);
        item.setRequest(itemRequest);
    }

    @Test
    public void addRequestTest() {
        ItemRequestDto itemRequestDto = generator.nextObject(ItemRequestDto.class);
        Mockito
                .when(usersRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        UserNotFoundException e = assertThrows(UserNotFoundException.class, () ->
                requestService.addRequest(itemRequestDto, 1L));
        assertEquals("Пользователя с ID = 1 не существует", e.getMessage());

    }

    @Test
    public void getRequestDtoTest() {
        Mockito
                .when(itemsRepository.findAllByRequestId(Mockito.anyLong()))
                .thenReturn(List.of(item));

        ItemRequestDto itemRequestDto = requestService.getRequestDto(itemRequest);
        assertEquals(itemRequestDto.getId(), itemRequest.getId());
        assertEquals(itemRequestDto.getItems().get(0).getId(), item.getId(), "список вещей не равен");
    }

    @Test
    public void getRequestFromUserTest() {
        Mockito
                .when(usersRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Mockito
                .when(requestRepository.findByRequestorId(1L))
                .thenReturn(Collections.singletonList(itemRequest));
        List<ItemRequest> itemRequests = requestService.getRequestFromUser(1L);
        Mockito.verify(usersRepository, Mockito.times(1))
                .findById(1L);
        Mockito.verify(requestRepository, Mockito.times(1))
                .findByRequestorId(1L);
    }

    @Test
    public void getRequestFromUserTestWithException() {
        Mockito
                .when(usersRepository.findById(1L))
                .thenReturn(Optional.empty());

        UserNotFoundException e = assertThrows(UserNotFoundException.class, () ->
                requestService.getRequestFromUser(1L));
        assertEquals("Пользователь с ID = 1 отсутствует", e.getMessage());
    }

    @Test
    public void getAllRequestsTest() {
        Mockito
                .when(requestRepository.findAll(1L, PageRequest.of(1,1)))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        Page<ItemRequest> itemRequests = requestService.getAllRequests(1L, 1,1);

        Mockito.verify(requestRepository, Mockito.times(1))
                .findAll(1L, PageRequest.of(1,1));

    }

    @Test
    public void getItemRequestTest() {
        Mockito
                .when(usersRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Mockito
                .when(requestRepository.findById(1L))
                .thenReturn(Optional.of(itemRequest));
        ItemRequest itemRequest1 = requestService.getItemRequest(1L,1L);
        Mockito.verify(usersRepository, Mockito.times(1))
                .findById(1L);
        Mockito.verify(requestRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    public void getItemRequestTestWithException() {
        Mockito
                .when(usersRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Mockito
                .when(requestRepository.findById(1L))
                .thenReturn(Optional.empty());

        NotFoundRequestException e = assertThrows(NotFoundRequestException.class, () ->
                requestService.getItemRequest(1L, 1L));
        assertEquals("Запрос с ID = 1 не найден", e.getMessage());
    }



}