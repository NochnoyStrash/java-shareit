package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private long identificate = 1;

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        Item item = mapToItem(itemDto, userId);
        item.setId(identificate);
        itemRepository.save(userId, item);
        identificate++;
        return mapToDto(item);
    }

    @Override
    public Item updateItem(ItemDto itemDto, long userId, long itemId) {
        if (itemDto.getId() != 0 && itemDto.getId() != itemId) {
            throw new ItemNotFoundException(String.format("Не найдена вещь с таким ID = %d", itemId));
        }
        Item item2 = itemRepository.findItemByUserId(userId).stream()
                .filter(item1 -> item1.getId() == itemId).findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("У пользователя с ID = %d  " +
                        "нет вещей с ID = %d", userId, itemId)));
        if (itemDto.getName() != null) {
            item2.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item2.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item2.setAvailable(itemDto.getAvailable());
        }
        return item2;
    }

    @Override
    public ItemDto getItemDto(long itemId, long userId) {
        Item item = itemRepository.getItem(userId, itemId);
        return mapToDto(item);
    }

    @Override
    public List<ItemDto> getMasterItemsDto(long userId) {
        List<ItemDto> itemDtos = new ArrayList<>();
        itemRepository.findItemByUserId(userId).forEach(item -> itemDtos.add(mapToDto(item)));
        return itemDtos;
    }

    @Override
    public List<ItemDto> findItemFromText(String text) {
        List<ItemDto> itemDtos = new ArrayList<>();
        List<Item> items = itemRepository.findFromText(text);
        items.forEach(i -> itemDtos.add(mapToDto(i)));
        return itemDtos;
    }

    private ItemDto mapToDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable());
    }

    private Item mapToItem(ItemDto dto, long userId) {
        return new Item(dto.getName(), dto.getDescription(), userId, dto.getAvailable());
    }
}
