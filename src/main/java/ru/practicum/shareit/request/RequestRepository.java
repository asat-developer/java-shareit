package ru.practicum.shareit.request;

public interface RequestRepository {
    ItemRequest getRequestById(Integer requestId);

    ItemRequest saveRequest(ItemRequest itemRequest);

    Boolean checkRequest(Integer requestId);
}
