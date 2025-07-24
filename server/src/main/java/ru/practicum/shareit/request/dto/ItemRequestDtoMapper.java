package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemReadDtoForRequests;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemRequestDtoMapper {

    public static ItemRequest itemRequestWriteDtoToItemRequest(ItemRequestWriteDto itemRequestWriteDto, User user) {
        ItemRequest request = new ItemRequest(user, LocalDateTime.now());
        request.setDescription(itemRequestWriteDto.getDescription());
        return request;
    }

    public static ItemRequestReadDto itemRequestToItemRequestReadDto(ItemRequest request) {
        ItemRequestReadDto requestDto = new ItemRequestReadDto(request.getId(),
                request.getDescription(),
                request.getRequestor().getId(),
                request.getTimeCreated());
        return requestDto;
    }

    public static List<ItemRequestReadDtoWithItems> rawResponseToDto(List<RequestWithItems> rawList) {
        Map<Integer, ItemRequestReadDtoWithItems> requestMap = new LinkedHashMap<>();

        for (RequestWithItems row : rawList) {
            ItemRequestReadDtoWithItems request = requestMap.computeIfAbsent(row.getRequestId(), id -> {
                ItemRequestReadDtoWithItems dtoRequest = new ItemRequestReadDtoWithItems(id,
                        row.getDescription(),
                        row.getRequestorId(),
                        row.getTimeCreated(),
                        new ArrayList<>());
                return dtoRequest;
            });

            if (row.getItemId() != null) {
                ItemReadDtoForRequests itemDto = new ItemReadDtoForRequests(row.getItemId(),
                        row.getName(),
                        row.getOwnerId());
                request.getItems().add(itemDto);
            }
        }
        return new ArrayList<>(requestMap.values());
    }
}
