package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestReadDto;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;

public interface RequestService {
    ItemRequestReadDto getRequestById(Integer requestId);

    ItemRequestReadDto saveRequest(ItemRequestWriteDto itemRequestWriteDto, Integer userId);
}
