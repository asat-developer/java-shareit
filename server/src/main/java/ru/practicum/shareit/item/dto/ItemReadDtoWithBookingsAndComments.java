package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingReadDto;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ItemReadDtoWithBookingsAndComments {
    private final Integer id;
    private final String name;
    private final String description;
    private final boolean available;
    private final Integer ownerId;
    private final Integer requestId;
    private final List<CommentReadDto> comments;
    private final BookingReadDto lastBooking;
    private final BookingReadDto nextBooking;
}
