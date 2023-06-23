package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class ItemsRepositoryTest {
    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RequestRepository requestRepository;

    @Test
    public void searchItemTest() {
        User user = new User(null, "Вася", "enot@mail.ru");
        usersRepository.save(user);
        Item item = new Item(null, "Дрель", "Мощная дрель", user, true, null);
        Item item2 = new Item(null, "Отвертка", "трещеточная отвертка", user, true, null);
        itemsRepository.save(item);
        itemsRepository.save(item2);
        List<Item> items = itemsRepository.searchText("дре", PageRequest.of(0,2)).getContent();
        List<Item> items1 = itemsRepository.searchText("Отвер", PageRequest.of(0,2)).getContent();
        Assertions.assertEquals(items1.get(0).getName(), item2.getName(), "вещи не равны");
        Assertions.assertEquals(items.get(0).getName(), item.getName(), "вещи не равны");
    }

    @Test
    public void findAllByOwnerIdTest() {
        User user = new User(null, "Вася", "enot@mail.ru");
        user = usersRepository.save(user);
        User user1 = new User(null, "Георгий", "movo@mail.ru");
        user1 = usersRepository.save(user1);
        Item item = new Item(null, "Дрель", "Мощная дрель", user, true, null);
        Item item2 = new Item(null, "Отвертка", "трещеточная отвертка", user1, true, null);
        itemsRepository.save(item);
        itemsRepository.save(item2);
        List<Item> items = itemsRepository.findAllByOwnerId(user.getId());
        List<Item> items1 = itemsRepository.findAllByOwnerId(user1.getId());
        Assertions.assertEquals(items.get(0).getOwner(), user, "владельцы не равны");
        Assertions.assertEquals(items1.get(0).getOwner(), user1, "владельцы не равны");
    }

    @Test
    public void findAllByRequestIdTest() {
        User user = new User(null, "Вася", "enot@mail.ru");
        usersRepository.save(user);
        User user1 = new User(null, "Георгий", "movo@mail.ru");
        usersRepository.save(user1);
        ItemRequest itemRequest = new ItemRequest(null, "нужна пила", user, LocalDateTime.now());
        itemRequest = requestRepository.save(itemRequest);
        Item item = new Item(null, "Дрель", "Мощная дрель", user1, true, itemRequest);
        itemsRepository.save(item);
        List<Item> items = itemsRepository.findAllByRequestId(itemRequest.getId());
        Assertions.assertEquals(items.get(0).getRequest(), itemRequest, "описание не равно");
    }
}