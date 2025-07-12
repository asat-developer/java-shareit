package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class CommentReadDto {
    private final Integer id;
    private final String text;
    private final Integer itemId;
    private final String authorName;
    private final LocalDateTime created;
}
