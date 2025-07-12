package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class BookingReadDto {
    private final Integer id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final ItemDtoForBooking item;
    private final BookerDtoForBooking booker;
    private final Status status;
}


