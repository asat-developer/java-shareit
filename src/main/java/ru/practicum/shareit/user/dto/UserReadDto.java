package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserReadDto {
    private final Integer id;
    private final String email;
    private final String name;
}
