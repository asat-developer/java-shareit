package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;

public class BookingDtoMapper {
    public static BookingReadDto bookingToBookingReadDto(Booking booking) {
        return new BookingReadDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getBookerId(),
                booking.getStatus());
    }

    public static Booking bookingWriteDtoToBooking(BookingWriteDto bookingWriteDto, Integer bookerId, boolean post) {
        Booking booking = new Booking(null,
                bookingWriteDto.getStart(),
                bookingWriteDto.getEnd(),
                bookingWriteDto.getItemId(),
                bookerId,
                Status.WAITING);
        return booking;
    }
}
