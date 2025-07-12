package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingReadDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemReadDtoWithBookingsAndComments getItemById(Integer id, Integer userId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Предмета с таким id нет"));
        List<CommentReadDto> comments = commentRepository.findByItemId(id).stream()
                .map(entity -> ItemDtoMapper.commentToCommentReadDto(entity))
                .collect(Collectors.toList());
        if (item.getOwner().getId().equals(userId)) {
            Booking lastBooking = bookingRepository.findTopByItemIdAndStartBefore(id, LocalDateTime.now()).orElse(null);
            Booking nextBooking = bookingRepository.findTopByItemIdAndEndAfter(id, LocalDateTime.now()).orElse(null);
            BookingReadDto lastBookingMapped = null;
            BookingReadDto nextBookingMapped = null;
            if (lastBooking != null) {
                lastBookingMapped = BookingDtoMapper.bookingToBookingReadDto(lastBooking);
            }
            if (nextBooking != null) {
                nextBookingMapped = BookingDtoMapper.bookingToBookingReadDto(nextBooking);
            }
            return ItemDtoMapper.itemToItemReadDtoWithBookingsAndComments(item,
                    comments,
                    lastBookingMapped,
                    nextBookingMapped);
        }
        return ItemDtoMapper.itemToItemReadDtoWithBookingsAndComments(item,
                comments,
                null,
                null);
    }

    @Override
    public ItemReadDto saveItem(ItemWriteDto itemWriteDto, Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        ItemRequest request;
        if (itemWriteDto.getOwnerId() == null) {
            request = null;
        } else {
            request = requestRepository.findById(itemWriteDto.getRequestId()).orElseThrow(() ->
                    new NotFoundException("Такого запроса не существует"));
        }
        User user = userRepository.findById(userId).get();
        Item item = ItemDtoMapper.itemWriteDtoToItem(itemWriteDto, user, request);
        itemRepository.save(item);
        return ItemDtoMapper.itemToItemReadDto(item);
    }

    @Override
    public ItemReadDto updateItem(ItemWriteDto itemWriteDto, Integer userId, Integer itemId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Такого предмента не существует");
        }
        if (!itemRepository.findById(itemId).get().getOwner().getId().equals(userId)) {
            throw new ValidationException("Нарушение прав !", HttpStatus.CONFLICT);
        }

        Item item = itemRepository.findById(itemId).get();
        if (itemWriteDto.getName() != null) {
            item.setName(itemWriteDto.getName());
        }
        if (itemWriteDto.getDescription() != null) {
            item.setDescription(itemWriteDto.getDescription());
        }
        if (itemWriteDto.getAvailable() != null) {
            item.setAvailable(itemWriteDto.getAvailable());
        }
        if (itemWriteDto.getRequestId() != null) {
            ItemRequest request = requestRepository.findById(itemWriteDto.getRequestId()).orElseThrow(() ->
                    new NotFoundException("Такого запроса не существует"));
            item.setRequest(request);
        }
        itemRepository.save(item);
        return ItemDtoMapper.itemToItemReadDto(item);
    }

    @Override
    public List<ItemReadDto> getAllItemsByUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return itemRepository.findByOwnerId(userId).stream()
                .map(item -> ItemDtoMapper.itemToItemReadDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemReadDto> searchByText(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        text = "%" + text.toUpperCase() + "%";
        return itemRepository.searchByText(text).stream()
                .map(item -> ItemDtoMapper.itemToItemReadDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemReadDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(item -> ItemDtoMapper.itemToItemReadDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public CommentReadDto saveComment(CommentWriteDto commentWriteDto, Integer userId, Integer itemId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Такой вещи не существует");
        }
        User user = userRepository.findById(userId).get();
        Item item = itemRepository.findById(itemId).get();
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())) {
            throw new ValidationException("Пользователь не брал в аренду эту вещь, либо бронирование ещё не завершилось !",
                    HttpStatus.BAD_REQUEST);
        }
        Comment comment = ItemDtoMapper.commentWriteDtoToComment(commentWriteDto, user, item);
        commentRepository.save(comment);
        return ItemDtoMapper.commentToCommentReadDto(comment);
    }
}
