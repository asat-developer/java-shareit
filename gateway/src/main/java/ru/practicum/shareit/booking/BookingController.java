package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingWriteDto;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private static final String HEADERNAME = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> saveBooking(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId,
                                              @RequestBody @Valid BookingWriteDto bookingWriteDto) {
        return bookingClient.saveBooking(userId, bookingWriteDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId,
                                        @PathVariable("bookingId") @Positive @NotNull Integer bookingId,
                                        @RequestParam("approved") @NotNull Boolean approvedStatus) {
        return bookingClient.updateBooking(userId, bookingId, approvedStatus);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingByBookingId(@RequestHeader(HEADERNAME) @Positive @NotNull Long userId,
                                                 @PathVariable("bookingId") @Positive @NotNull Long bookingId) {
        return bookingClient.getBookingByBookingId(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") String stateStr) {
        State state = State.from(stateStr);
        return bookingClient.getBookingsByUser(userId, state);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId,
                                                   @RequestParam(value = "state", defaultValue = "ALL") String stateStr) {
        State state = State.from(stateStr);
        return bookingClient.getBookingsByOwner(userId, state);
    }
}
