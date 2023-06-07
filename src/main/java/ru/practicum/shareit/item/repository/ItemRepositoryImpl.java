package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<Long>> itemsIdFromUser = new HashMap<>();
    private final Map<Long, Item> itemMap = new HashMap<>();
    private final UserService userService;

    @Autowired
    public ItemRepositoryImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<Item> findItemByUserId(long userId) {
        if (!itemsIdFromUser.containsKey(userId)) {
            throw new UserNotFoundException(String.format("У пользователя с ID = %d нет вещей", userId));
        }
        userService.getUser(userId);
        List<Item> itemList = new ArrayList<>();
        itemsIdFromUser.get(userId).forEach(i -> itemList.add(itemMap.get(i)));
        return itemList;
    }

    @Override
    public Item save(long userId, Item item) {
        userService.getUser(userId);
        itemMap.put(item.getId(), item);
        itemsIdFromUser.compute(userId, (usersId, userItemsId) -> {
            if (userItemsId == null) {
                userItemsId = new ArrayList<>();
            }
            userItemsId.add(item.getId());
            return userItemsId;
        });
        return item;
    }

    @Override
    public Item getItem(long userId, long itemId) {
        if (!itemMap.containsKey(itemId)) {
            throw new ItemNotFoundException(String.format("Отсутсвует вещь с ID = %d", itemId));
        }
        return itemMap.get(itemId);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        itemMap.remove(itemId);
        itemsIdFromUser.get(userId).remove(itemId);
        if (itemsIdFromUser.get(userId).isEmpty()) {
            itemsIdFromUser.remove(userId);
        }
    }

    @Override
    public List<Item> findFromText(String text) {
        List<Item> itemList = new ArrayList<>();
        if (text.isEmpty()) {
            return itemList;
        }
        final String text1 = text.toLowerCase();
        return itemMap.values().stream().filter(item -> isContain(item, text1))
                .sorted(Comparator.comparingInt(o -> Integer.valueOf(Math.toIntExact(o.getId())))).collect(Collectors.toList());
    }

    private boolean isContain(Item item, String text) {
        return (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text))
                && item.isAvailable();
    }

}
