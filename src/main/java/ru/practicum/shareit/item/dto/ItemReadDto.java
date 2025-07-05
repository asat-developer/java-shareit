package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@RequiredArgsConstructor
public class ItemReadDto {
    private final Integer id;
    private final String name;
    private final String description;
    private final boolean available;
    private final Integer ownerId;
    private final Integer requestId;
}
