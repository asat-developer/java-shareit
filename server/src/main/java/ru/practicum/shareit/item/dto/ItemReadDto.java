package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ItemReadDto {
    private final Integer id;
    private final String name;
    private final String description;
    private final boolean available;
    private final Integer ownerId;
    private final Integer requestId;
}
