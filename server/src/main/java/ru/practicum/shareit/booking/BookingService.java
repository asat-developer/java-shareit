package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingReadDto;
import ru.practicum.shareit.booking.dto.BookingWriteDto;

import java.util.List;

public interface BookingService {

    BookingReadDto saveBooking(BookingWriteDto bookingWriteDto, Integer bookerId);

    BookingReadDto updateBooking(Integer bookerId, Integer bookingId, Boolean approvedStatus);

    BookingReadDto getBookingByBookingId(Integer userId, Integer bookingId);

    List<BookingReadDto> getBookingsByUser(Integer userId, State state);

    List<BookingReadDto> getBookingsByOwner(Integer userId, State state);
}