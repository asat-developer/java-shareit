package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ItemRequestReadDto {
    private final Integer id;
    private final String description;
    private final Integer requestorId;
    private final LocalDateTime timeCreated;
}
