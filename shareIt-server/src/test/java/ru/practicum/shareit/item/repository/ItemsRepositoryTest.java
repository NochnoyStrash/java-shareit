package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemsRepositoryTest {
    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RequestRepository requestRepository;
    User user;
    User user1;
    Item item;
    Item item2;
    Item item3;
    ItemRequest itemRequest;

    @BeforeAll
    public void beforeAll() {
        user = new User(null, "Вася", "enot@mail.ru");
        usersRepository.save(user);
        user1 = new User(null, "Георгий", "movo@mail.ru");
        usersRepository.save(user1);
        item = new Item(null, "Дрель", "Мощная дрель", user, true, null);
        item2 = new Item(null, "Отвертка", "трещеточная отвертка", user, true, null);
        itemsRepository.save(item);
        itemsRepository.save(item2);
        itemRequest = new ItemRequest(null, "нужна пила", user, LocalDateTime.now());
        itemRequest = requestRepository.save(itemRequest);
        item3 = new Item(null, "Дрель", "Мощная дрель", user1, true, itemRequest);
        itemsRepository.save(item3);

    }

    @Test
    public void searchItemTest() {
        List<Item> items = itemsRepository.searchText("дре", PageRequest.of(0,2)).getContent();
        List<Item> items1 = itemsRepository.searchText("Отвер", PageRequest.of(0,2)).getContent();
        Assertions.assertEquals(items1.get(0).getName(), item2.getName(), "вещи не равны");
        Assertions.assertEquals(items.get(0).getName(), item.getName(), "вещи не равны");
    }

    @Test
    public void findAllByOwnerIdTest() {
        List<Item> items = itemsRepository.findAllByOwnerId(user.getId());
        List<Item> items1 = itemsRepository.findAllByOwnerId(user1.getId());
        Assertions.assertEquals(items.get(0).getOwner().getId(), user.getId(), "владельцы не равны");
        Assertions.assertEquals(items1.get(0).getOwner().getId(), user1.getId(), "владельцы не равны");
    }

    @Test
    public void findAllByRequestIdTest() {
        List<Item> items = itemsRepository.findAllByRequestId(itemRequest.getId());
        Assertions.assertEquals(items.get(0).getRequest().getId(), itemRequest.getId(), "описание не равно");
    }
}