package ru.practicum.shareit.request;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.repository.ItemsRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestRepositoryTest {
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    UsersRepository usersRepository;
    private final EasyRandom random = new EasyRandom();

    User user;
    User user1;
    ItemRequest itemRequest;
    ItemRequest itemRequest1;
    ItemRequest itemRequest2;


    @BeforeEach

    public void beforeAll() {
        user = new User(null, "Вася", "voyfh@mail.ru");
        user = usersRepository.save(user);
        user1 = new User(null, "Коля", "murt@mail.ru");
        user1 = usersRepository.save(user1);

        itemRequest = new ItemRequest(null, "нужна пила", user, LocalDateTime.now());
        itemRequest = requestRepository.save(itemRequest);
        itemRequest1 = new ItemRequest(null, "нужен сварочный аппараь", user, LocalDateTime.now());
        itemRequest1 = requestRepository.save(itemRequest1);
        itemRequest2 = new ItemRequest(null, "нужна балгарка",user1, LocalDateTime.now());
        itemRequest2 = requestRepository.save(itemRequest2);

    }

    @Test
    public void findByRequestorIdTest() {
        List<ItemRequest> itemRequests = requestRepository.findByRequestorId(user.getId());

        assertEquals(itemRequests.size(), 2, "количество элементов в списке не равно 2");
        assertEquals(itemRequests.get(0).getId(), itemRequest.getId(), "запросы не равны");
        assertEquals(itemRequests.get(1).getId(), itemRequest1.getId(), "запросы не равны");
    }

    @Test
    public void findAllTest() {
        List<ItemRequest> itemRequests2 = requestRepository.findAll(user1.getId(), Pageable.unpaged()).getContent();

        assertEquals(itemRequests2.size(), 2, "количество элементов в списке не равно 2");
        assertNotEquals(itemRequests2.get(0).getId(), itemRequest.getId(), "запросы равны");
        assertNotEquals(itemRequests2.get(1).getId(), itemRequest1.getId(), "запросы равны");

    }

}