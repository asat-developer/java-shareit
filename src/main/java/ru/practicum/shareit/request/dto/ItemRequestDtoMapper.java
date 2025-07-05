package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;

public class ItemRequestDtoMapper {
    public static ItemRequestReadDto itemRequestToItemRequestReadDto(ItemRequest itemRequest) {
        return new ItemRequestReadDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestorId(),
                itemRequest.getTimeCreated());
    }

    public static ItemRequest itemRequestWriteDtoToItemRequest(ItemRequestWriteDto itemRequestWriteDto, Integer userId) {
        return new ItemRequest(null,
                itemRequestWriteDto.getDescription(),
                userId,
                LocalDateTime.now());
    }
}
