package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;

public interface RequestWithItems {
    Integer getRequestId();

    String getDescription();

    Integer getRequestorId();

    LocalDateTime getTimeCreated();

    Integer getItemId();

    String getName();

    Integer getOwnerId();
}
