package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequest {
    private Integer id;
    private String description;
    private Integer requestorId;
    private LocalDateTime timeCreated;
}
