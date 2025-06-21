package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingRepository {
    List<Booking> getBookingsByItemId(Integer itemId);

    Booking saveBooking(Booking booking);

    Booking updateBookingByOwner(Booking booking);

    Booking updateBookingByBooker(Booking newBooking);

    Booking getBookingById(Integer bookingId);
}
