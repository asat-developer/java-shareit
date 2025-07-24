package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.dto.ItemReadDtoForRequests;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ItemRequestReadDtoWithItems {
    private final Integer id;
    private final String description;
    private final Integer requestorId;
    private final LocalDateTime created;
    private final List<ItemReadDtoForRequests> items;
}
