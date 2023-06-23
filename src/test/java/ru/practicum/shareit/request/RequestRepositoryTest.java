package ru.practicum.shareit.request;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.repository.ItemsRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RequestRepositoryTest {
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    UsersRepository usersRepository;
    private final EasyRandom random = new EasyRandom();

    @Test
    public void findByRequestorIdTest() {
        User user = new User(null, "Вася", "enot@mail.ru");
        user = usersRepository.save(user);

        ItemRequest itemRequest = new ItemRequest(null, "нужна пила", user, LocalDateTime.now());
        itemRequest = requestRepository.save(itemRequest);
        ItemRequest itemRequest1 = new ItemRequest(null, "нужен сварочный аппараь", user, LocalDateTime.now());
        itemRequest1 = requestRepository.save(itemRequest1);
        List<ItemRequest> itemRequests = requestRepository.findByRequestorId(user.getId());

        assertEquals(itemRequests.size(), 2, "количество элементов в списке не равно 2");
        assertEquals(itemRequests.get(0), itemRequest, "запросы не равны");
        assertEquals(itemRequests.get(1), itemRequest1, "запросы не равны");
    }

    @Test
    public void findAllTest() {
        User user = new User(null, "Вася", "enot@mail.ru");
        user = usersRepository.save(user);

        ItemRequest itemRequest = new ItemRequest(null, "нужна пила", user, LocalDateTime.now());
        itemRequest = requestRepository.save(itemRequest);
        ItemRequest itemRequest1 = new ItemRequest(null, "нужен сварочный аппараь", user, LocalDateTime.now());
        itemRequest1 = requestRepository.save(itemRequest1);
        List<ItemRequest> itemRequests = requestRepository.findByRequestorId(user.getId());
        User user1 = new User(null, "Илья", "varya@mail.ru");
        user1 = usersRepository.save(user1);

        ItemRequest itemRequest2 = new ItemRequest(null, "нужен алкометр", user1, LocalDateTime.now());
        itemRequest2 = requestRepository.save(itemRequest2);

        List<ItemRequest> itemRequests2 = requestRepository.findAll(user1.getId(), Pageable.unpaged()).getContent();
        assertEquals(itemRequests2.size(), 2, "количество элементов в списке не равно 2");
        assertNotEquals(itemRequests2.get(0), itemRequest2, "запросы равны");
        assertNotEquals(itemRequests2.get(1), itemRequest2, "запросы равны");

    }



}