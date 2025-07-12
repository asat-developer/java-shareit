package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingDtoMapper {
    public static BookingReadDto bookingToBookingReadDto(Booking booking) {
        return new BookingReadDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new ItemDtoForBooking(booking.getItem().getId(), booking.getItem().getName()),
                new BookerDtoForBooking(booking.getBooker().getId()),
                booking.getStatus());
    }

    public static Booking bookingWriteDtoToBooking(BookingWriteDto bookingWriteDto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setStart(bookingWriteDto.getStart());
        booking.setEnd(bookingWriteDto.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return booking;
    }
}
