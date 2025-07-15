package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemRequestDtoMapper {
    public static ItemRequestReadDto itemRequestToItemRequestReadDto(ItemRequest itemRequest) {
        return new ItemRequestReadDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor().getId(),
                itemRequest.getTimeCreated());
    }

    public static ItemRequest itemRequestWriteDtoToItemRequest(ItemRequestWriteDto itemRequestWriteDto, User user) {
        ItemRequest request = new ItemRequest(user, LocalDateTime.now());
        request.setDescription(itemRequestWriteDto.getDescription());
        return request;
    }
}
