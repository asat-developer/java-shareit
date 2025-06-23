package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingReadDto;
import ru.practicum.shareit.booking.dto.BookingWriteDto;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<BookingReadDto> getBookingsByItemId(Integer itemId) {
        if (!itemRepository.checkItem(itemId)) {
            throw new NotFoundException("Нет предмета с таким id");
        }

        return bookingRepository.getBookingsByItemId(itemId).stream()
                .map(booking -> BookingDtoMapper.bookingToBookingReadDto(booking))
                .collect(Collectors.toList());
    }

    @Override
    public BookingReadDto saveBooking(BookingWriteDto bookingWriteDto, Integer bookerId) {
        if (!itemRepository.checkItem(bookingWriteDto.getItemId())) {
            throw new NotFoundException("Заказываемого предмета нет !");
        }
        if (!userRepository.checkUser(bookerId)) {
            throw new NotFoundException("Пользователя, который запрашивает операцию нет !");
        }
         return BookingDtoMapper.bookingToBookingReadDto(bookingRepository
                 .saveBooking(BookingDtoMapper.bookingWriteDtoToBooking(bookingWriteDto, bookerId, true)));
    }

    @Override
    public BookingReadDto updateBooking(BookingWriteDto bookingWriteDto, Integer bookingId, Integer userId) {
        Booking booking = bookingRepository.getBookingById(bookingId);
        if (!userRepository.checkUser(userId)) {
            throw new NotFoundException("Пользователя, который запрашивает операцию нет !");
        }
        if (bookingWriteDto.getStatus() == Status.UNKNOWN) {
            throw new ValidationException("Задан неверный статус для обновления !");
        }
        if (booking.getBookerId().equals(userId) && bookingWriteDto.getStatus() == Status.CANCELLED) {
            // Бронирование при обновлении создателем бронирования измениться может тольео на CANCELLED
            booking.setStatus(Status.CANCELLED);
            return BookingDtoMapper.bookingToBookingReadDto(bookingRepository.updateBookingByBooker(booking));
        } else if (itemRepository.getItemById(booking.getItemId()).getOwnerId().equals(userId) // Изменение бронирование владельцем вещи
                && (bookingWriteDto.getStatus() == Status.APPROVED ||
                bookingWriteDto.getStatus() == Status.REJECTED)) {
            booking.setStatus(bookingWriteDto.getStatus());
            return BookingDtoMapper.bookingToBookingReadDto(bookingRepository.updateBookingByOwner(booking));
        } else if (!booking.getBookerId().equals(userId) && !booking.getBookerId()
                .equals(itemRepository.getItemById(booking.getItemId()).getOwnerId())) {
            throw new ValidationException("Обращающийся пользователь не является владельцем предмета или заказчиком !");
        } else {
            throw new ValidationException("Пользователь пытается совершить неразрешенную для его статуса операцию !");
        }
    }
}
