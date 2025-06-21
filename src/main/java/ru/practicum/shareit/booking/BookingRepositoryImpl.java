package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class BookingRepositoryImpl implements BookingRepository {

    private final Map<Integer, Booking> bookings = new HashMap<>();

    @Override
    public List<Booking> getBookingsByItemId(Integer itemId) {
        return bookings.values().stream()
                .filter(booking -> booking.getItemId().equals(itemId))
                .collect(Collectors.toList());
    }

    @Override
    public Booking saveBooking(Booking booking) {
        booking.setId(getNextId());
        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public Booking updateBookingByOwner(Booking newBooking) {
        bookings.put(newBooking.getId(), newBooking);
        return newBooking;
    }

    @Override
    public Booking updateBookingByBooker(Booking newBooking) {
        bookings.put(newBooking.getId(), newBooking);
        return newBooking;
    }

    @Override
    public Booking getBookingById(Integer bookingId) {
        return bookings.get(bookingId);
    }

    private Integer getNextId() {
        return bookings.keySet().stream()
                .max((o1, o2) -> Integer.compare(o1, o2))
                .orElse(0) + 1;
    }
}
