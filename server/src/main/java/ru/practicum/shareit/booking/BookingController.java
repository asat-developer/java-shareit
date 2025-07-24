package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingReadDto;
import ru.practicum.shareit.booking.dto.BookingWriteDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String HEADERNAME = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public BookingReadDto saveBooking(@RequestHeader(HEADERNAME) Integer userId,
                                      @RequestBody BookingWriteDto bookingWriteDto) {
        return bookingService.saveBooking(bookingWriteDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingReadDto updateBooking(@RequestHeader(HEADERNAME) Integer userId,
                                        @PathVariable("bookingId") Integer bookingId,
                                        @RequestParam("approved") Boolean approvedStatus) {
        return bookingService.updateBooking(userId, bookingId, approvedStatus);
    }

    @GetMapping("/{bookingId}")
    public BookingReadDto getBookingByBookingId(@RequestHeader(HEADERNAME) Integer userId,
                                                 @PathVariable("bookingId") Integer bookingId) {
        return bookingService.getBookingByBookingId(userId, bookingId);
    }

    @GetMapping
    public List<BookingReadDto> getBookingsByUser(@RequestHeader(HEADERNAME) Integer userId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") State state) {
        return bookingService.getBookingsByUser(userId, state);
    }


    @GetMapping("/owner")
    public List<BookingReadDto> getBookingsByOwner(@RequestHeader(HEADERNAME) Integer userId,
                                             @RequestParam(value = "state", defaultValue = "ALL") State state) {
        return bookingService.getBookingsByOwner(userId, state);
    }
}
