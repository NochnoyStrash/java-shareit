package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Long, List<Long>> itemsIdFromUser = new HashMap<>();
    private Map<Long, Item> itemMap = new HashMap<>();
    private final UserRepository userRepository;

    @Autowired
    public ItemRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<Item> findItemByUserId(long userId) {
        if (!itemsIdFromUser.containsKey(userId)) {
            throw new UserNotFoundException("у пользователя с ID = " + userId + " нет вещей");
        }
        userRepository.getUser(userId);
        List<Item> itemList = new ArrayList<>();
        for (Long id : itemsIdFromUser.get(userId)) {
            itemList.add(itemMap.get(id));
        }
        return itemList;
    }

    @Override
    public Item save(long userId, Item item) {
        userRepository.getUser(userId);
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
            throw new ItemNotFoundException("Отсутсвует вещь с ID = " + itemId);
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
        return itemMap.values().stream().filter(item -> (item.getName().toLowerCase().contains(text1)
                || item.getDescription().toLowerCase().contains(text1))
                && item.isAvailable()).sorted(Comparator.comparingInt(o -> (int) o.getId())).collect(Collectors.toList());
    }

}
