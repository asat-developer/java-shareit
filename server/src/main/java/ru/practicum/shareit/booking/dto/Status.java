package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Status {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELLED,
    @JsonEnumDefaultValue
    UNKNOWN
}
