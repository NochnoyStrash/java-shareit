package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingInfo;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLastAndNextBooking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.CommentsValidateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemsRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.exception.NotFoundRequestException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemsRepository itemsRepository;
    private final UserService usersService;
    private  final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;
    private long identificate = 1;

    @Override
    @Transactional
    public ItemDto addItem(ItemDto itemDto, long userId) {
        User user = usersService.getUser(userId);

        Item item = mapToItem(itemDto, user);
        item = itemsRepository.save(item);
        return ItemMapper.mapToDto(item);
    }

    @Override
    @Transactional
    public Item updateItem(ItemDto itemDto, long userId, long itemId) {
        Item item2 = itemsRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Не найдена вещь с таким ID = %d", itemId)));
        if (item2.getOwner().getId() != userId) {
            throw new ItemNotFoundException(String.format("У пользователя с ID = %d  " +
                    "нет вещей с ID = %d", userId, itemId));
        }

        if (itemDto.getName() != null) {
            item2.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item2.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item2.setAvailable(itemDto.getAvailable());
        }

        return itemsRepository.save(item2);
    }

    public Item getItem(Long itemId) {
        Item item = itemsRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Не найдена вещь с таким ID = %d", itemId)));

        return item;
    }


    @Override
    public ItemDto getItemDto(long itemId) {
        return ItemMapper.mapToDto(getItem(itemId));
    }

    public ItemDtoLastAndNextBooking getItemLastNext(Long itemId,  Long ownerId) {
        Item item = getItem(itemId);
        List<CommentDto> commentDtos = new ArrayList<>();
        List<Comment> comments = commentRepository.findComments(itemId);
        comments.forEach(c -> commentDtos.add(CommentMapper.mapToCommentDto(c)));
        ItemDtoLastAndNextBooking itemDto = ItemDtoLastAndNextBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .comments(commentDtos)
                .build();
        List<Booking> lasts = bookingRepository.findLastBookingItem(itemId);
        if (!lasts.isEmpty()) {
            Booking booking = lasts.get(0);
            if (booking.getItem().getOwner().getId().equals(ownerId)) {
                itemDto.setLastBooking(new BookingInfo(booking.getId(), booking.getBooker().getId()));
            }
        }
        List<Booking> next = bookingRepository.findNextBookingItem(itemId);
        if (!next.isEmpty()) {
            Booking booking = next.get(0);
            if (booking.getItem().getOwner().getId().equals(ownerId) && booking.getStatus() == StatusBooking.APPROVED) {
                itemDto.setNextBooking(new BookingInfo(booking.getId(), booking.getBooker().getId()));
            }
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getMasterItemsDto(long userId) {
        List<ItemDto> itemDtos = new ArrayList<>();
        itemsRepository.findAllByOwnerId(userId).forEach(item -> itemDtos.add(ItemMapper.mapToDto(item)));
        return itemDtos;
    }

    @Override
    public List<ItemDto> findItemFromText(String text, Integer from, Integer size) {
        List<ItemDto> itemDtos = new ArrayList<>();
        if (text.isEmpty()) {
            return itemDtos;
        }
        int page = from / size;
        int pageLast = from % size;
        if (pageLast > 0) {
            page++;
        }
        Page<Item> itemName = itemsRepository.searchText(text, PageRequest.of(page, size));
        itemName.forEach(i -> itemDtos.add(ItemMapper.mapToDto(i)));
       return itemDtos.stream().filter(ItemDto::getAvailable).collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoLastAndNextBooking> findItemWithBookingLastNext(Long ownerId, Integer from, Integer size) {
        int page = from / size;
        int pageLast = from % size;
        if (pageLast > 0) {
            page++;
        }
        List<ItemDtoLastAndNextBooking> list = new ArrayList<>();
        List<Item> items = itemsRepository.findAllByOwnerId(ownerId);
        List<Booking> bookingsFuture = bookingRepository.findAllByOwnerFuture(ownerId);
        Collections.reverse(bookingsFuture);
        List<Booking> bookingsLast = bookingRepository.findAllByOwnerPost(ownerId);
        for (Item item : items) {
            Optional<Booking> future = bookingsFuture.stream().filter(b -> {
                return b.getItem().getId().equals(item.getId()) && b.getItem().getOwner().getId().equals(ownerId);
            }).findFirst();

            Optional<Booking> last = bookingsLast.stream().filter(b -> {
                return b.getItem().getId().equals(item.getId()) && b.getItem().getOwner().getId().equals(ownerId);
            }).findFirst();
            ItemDtoLastAndNextBooking itemDto = ItemDtoLastAndNextBooking.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.isAvailable())
                    .build();
            if (last.isPresent()) {
                Booking booking = last.get();
                itemDto.setLastBooking(new BookingInfo(booking.getId(), booking.getBooker().getId()));
            }
            future.ifPresent(nextBooking -> itemDto.setNextBooking(new BookingInfo(future.get().getId(), future.get().getBooker().getId())));
            list.add(itemDto);
        }
        list = list.stream().sorted(Comparator.comparing(ItemDtoLastAndNextBooking::getId)).collect(Collectors.toList());

        return new PageImpl<>(list, PageRequest.of(page, size), list.size()).getContent();

    }

    @Override
    @Transactional
    public Comment addComment(Long itemId, Long userId, CommentDto commentDto) {
        List<Booking> bookings = bookingRepository.findPastBookingByUser(itemId, userId);
        if (bookings.isEmpty()) {
            throw new CommentsValidateException("Комментарии могут оставлять только пользователи вещи с заверщенным заказом");
        }
        Item item = bookings.get(0).getItem();
        User author = bookings.get(0).getBooker();
        String text = commentDto.getText();
        Comment comment = CommentMapper.getComment(text, item, author);
        return commentRepository.save(comment);
    }

    @Override
    public CommentDto getComment(Comment comment) {
        return CommentMapper.mapToCommentDto(comment);
    }



    private Item mapToItem(ItemDto dto, User user) {
        Item item = Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .owner(user)
                .available(dto.getAvailable())
                .build();
        if (dto.getRequestId() != null) {
            ItemRequest itemRequest = requestRepository.findById(dto.getRequestId()).orElseThrow(() ->  new NotFoundRequestException(String.format("Запрос с ID = %d не найден", dto.getRequestId())));
            item.setRequest(itemRequest);
        }
        return item;
    }
}
