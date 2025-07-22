package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestReadDto;
import ru.practicum.shareit.request.dto.ItemRequestReadDtoWithItems;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;

import java.util.List;

public interface RequestService {
    //ItemRequestReadDtoWithItems getRequestById(Integer requestId);

    ItemRequestReadDto saveRequest(ItemRequestWriteDto itemRequestWriteDto, Integer userId);

    List<ItemRequestReadDtoWithItems> getRequestsByUserId(Integer userId);

    List<ItemRequestReadDto> findAllRequests();

    ItemRequestReadDtoWithItems getRequestById(Integer requestId);
}
