package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemReadDtoForRequests {
    private final Integer id;
    private final String name;
    private final Integer ownerId;
}
