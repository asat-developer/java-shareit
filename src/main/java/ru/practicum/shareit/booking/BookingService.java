package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingReadDto;
import ru.practicum.shareit.booking.dto.BookingWriteDto;

import java.util.List;

public interface BookingService {

    List<BookingReadDto> getBookingsByItemId(Integer itemId);

    BookingReadDto saveBooking(BookingWriteDto bookingWriteDto, Integer bookingId);

    BookingReadDto updateBooking(BookingWriteDto bookingWriteDto, Integer bookingId, Integer userId);
}
