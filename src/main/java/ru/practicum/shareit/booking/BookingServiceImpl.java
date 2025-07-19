package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingReadDto;
import ru.practicum.shareit.booking.dto.BookingWriteDto;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingReadDto saveBooking(BookingWriteDto bookingWriteDto, Integer bookerId) {
        Item item = itemRepository.findById(bookingWriteDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Нет запрашиваемого предмета !"));

        User booker = userRepository.findById(bookerId).orElseThrow(() ->
                new NotFoundException("Пользователя, который создает бронирование, нет !"));
        if (!item.getAvailable()) {
           throw new ValidationException("Предмет недоступен для заказа !", HttpStatus.BAD_REQUEST);
        }
        Booking booking = BookingDtoMapper.bookingWriteDtoToBooking(bookingWriteDto, booker, item);

        bookingRepository.save(booking);
        return BookingDtoMapper.bookingToBookingReadDto(booking);
    }

    @Override
    public BookingReadDto updateBooking(Integer userId, Integer bookingId, Boolean approvedStatus) {
        if (!userRepository.existsById(userId)) {
            throw new ValidationException("Пользователя, который запрашивает операцию, нет !", HttpStatus.BAD_REQUEST);
        }
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Нет такого заказа !"));
        Item item = itemRepository.findById(booking.getItem().getId()).get();
        if (!item.getOwner().getId().equals(userId)) {
            throw new ValidationException("Нарушение прав !", HttpStatus.CONFLICT);
        }
        if (approvedStatus) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingDtoMapper.bookingToBookingReadDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingReadDto getBookingByBookingId(Integer userId, Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Нет такого заказа !"));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ValidationException("Нарушение прав !", HttpStatus.NOT_FOUND);
        }
        return BookingDtoMapper.bookingToBookingReadDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingReadDto> getBookingsByUser(Integer userId, State state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя, который запрашивает операцию, нет !");
        }
        if (state == State.ALL) {
            return bookingRepository.findByBookerId(userId).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        } else if (state == State.CURRENT) {
            return bookingRepository.findByBookerIdAndByStartAndEndBetween(userId).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        } else if (state == State.PAST) {
            return bookingRepository.findByBookerIdAndEndBefore(userId, LocalDateTime.now()).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        } else if (state == State.FUTURE) {
            return bookingRepository.findByBookerIdAndStartAfter(userId, LocalDateTime.now()).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        } else if (state == State.WAITING) {
            return bookingRepository.findByBookerIdAndStatusIs(userId, Status.WAITING).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        } else if (state == State.REJECTED) {
            return bookingRepository.findByBookerIdAndStatusIs(userId, Status.REJECTED).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingReadDto> getBookingsByOwner(Integer userId, State state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя, который запрашивает операцию, нет !");
        }
        if (state == State.ALL) {
            return bookingRepository.findByItemOwnerId(userId).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        } else if (state == State.CURRENT) {
            return bookingRepository.findByOwnerIdAndByStartAndEndBetween(userId).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        } else if (state == State.PAST) {
            return bookingRepository.findByItemOwnerIdAndStartAfter(userId, LocalDateTime.now()).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        } else if (state == State.FUTURE) {
            return bookingRepository.findByItemOwnerIdAndEndBefore(userId, LocalDateTime.now()).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        } else if (state == State.WAITING) {
            return bookingRepository.findByItemOwnerIdAndStatusIs(userId, Status.WAITING).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        } else if (state == State.REJECTED) {
            return bookingRepository.findByItemOwnerIdAndStatusIs(userId, Status.REJECTED).stream()
                    .map(entity -> BookingDtoMapper.bookingToBookingReadDto(entity))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
