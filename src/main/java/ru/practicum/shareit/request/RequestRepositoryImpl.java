package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RequestRepositoryImpl implements RequestRepository {

    private final Map<Integer, ItemRequest> requests = new HashMap<>();

    @Override
    public ItemRequest getRequestById(Integer requestId) {
        return requests.get(requestId);
    }

    @Override
    public ItemRequest saveRequest(ItemRequest itemRequest) {
        itemRequest.setId(getNextId());
        requests.put(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public Boolean checkRequest(Integer requestId) {
        return requests.containsKey(requestId);
    }

    private Integer getNextId() {
        return requests.keySet().stream()
                .max((o1, o2) -> Integer.compare(o1, o2))
                .orElse(0) + 1;
    }
}
