package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingReadDto;
import ru.practicum.shareit.booking.dto.BookingWriteDto;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{itemId}")
    public List<BookingReadDto> getBookingsByItemId(@PathVariable("itemId") Integer itemId) {
        return bookingService.getBookingsByItemId(itemId);
    }

    @PostMapping
    public BookingReadDto saveBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @RequestBody BookingWriteDto bookingWriteDto) {
        return bookingService.saveBooking(bookingWriteDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingReadDto updateBooking(@PathVariable("bookingId") Integer bookingId,
                                        @RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @RequestBody BookingWriteDto bookingWriteDto) {
        return bookingService.updateBooking(bookingWriteDto, bookingId, userId);
    }
}
