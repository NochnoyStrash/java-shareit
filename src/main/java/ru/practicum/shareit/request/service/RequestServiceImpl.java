package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemsRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.exception.NotFoundRequestException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ItemsRepository itemsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public ItemRequest addRequest(ItemRequestDto requestDto, Long userId) {
        User user = usersRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователя с ID = %d не существует", userId)));
        ItemRequest itemRequest = RequestMapper.getItemRequest(requestDto, user);
        return requestRepository.save(itemRequest);
    }

    public ItemRequestDto getRequestDto(ItemRequest request) {
        List<ItemDto> itemForRequests = new ArrayList<>();
        List<Item> itemsFromRequest = itemsRepository.findAllByRequestId(request.getId());
        itemsFromRequest.forEach(item -> itemForRequests.add(ItemMapper.mapToDto(item)));
        return RequestMapper.getItemRequestDto(request, itemForRequests);
    }

    public List<ItemRequest> getRequestFromUser(Long userId) {
        usersRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с ID = %d отсутствует", userId)));
        List<ItemRequest> itemRequests = requestRepository.findByRequestorId(userId);
        itemRequests.sort(Comparator.comparing(ItemRequest::getCreated).reversed());
        return itemRequests;
    }

    public Page<ItemRequest> getAllRequests(Long userId, Integer from, Integer size) {
        return requestRepository.findAll(userId, PageRequest.of(from, size));
    }

    public ItemRequest getItemRequest(Long requestId, Long userId) {
        usersRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с ID = %d отсутствует", userId)));
        return requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundRequestException(String.format("Запрос с ID = %d не найден", requestId)));
    }
}
