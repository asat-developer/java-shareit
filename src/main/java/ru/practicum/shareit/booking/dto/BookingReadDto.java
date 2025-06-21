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
    private final Integer itemId;
    private final Integer bookerId;
    private final Status status;
}
