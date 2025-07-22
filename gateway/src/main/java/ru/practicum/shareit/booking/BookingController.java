package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingWriteDto;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String HEADERNAME = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> saveBooking(@RequestHeader(HEADERNAME) Integer userId,
                                              @RequestBody @Validated(BookingWriteDto.OnPost.class) BookingWriteDto bookingWriteDto) {
        return bookingClient.saveBooking(userId, bookingWriteDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(HEADERNAME) Integer userId,
                                        @PathVariable("bookingId") Integer bookingId,
                                        @RequestParam("approved") Boolean approvedStatus) {
        return bookingClient.updateBooking(userId, bookingId, approvedStatus);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingByBookingId(@RequestHeader(HEADERNAME) Integer userId,
                                                 @PathVariable("bookingId") Integer bookingId) {
        return bookingClient.getBookingByBookingId(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader(HEADERNAME) Integer userId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") State state) {
        return bookingClient.getBookingsByUser(userId, state);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader(HEADERNAME) Integer userId,
                                                   @RequestParam(value = "state", defaultValue = "ALL") State state) {
        return bookingClient.getBookingsByOwner(userId, state);
    }
}
