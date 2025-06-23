package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestReadDto;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String HEADERNAME = "X-Sharer-User-Id";

    private final RequestService requestService;

    @GetMapping("/{requestId}")
    public ItemRequestReadDto getRequestById(@PathVariable("requestId") Integer requestId) {
        return requestService.getRequestById(requestId);
    }

    @PostMapping
    public ItemRequestReadDto saveItemRequest(@RequestHeader(HEADERNAME) Integer userId,
                                              @RequestBody ItemRequestWriteDto itemRequestWriteDto) {
        return requestService.saveRequest(itemRequestWriteDto, userId);
    }
}
