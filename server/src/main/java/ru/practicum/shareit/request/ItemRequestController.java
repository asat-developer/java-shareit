package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestReadDto;
import ru.practicum.shareit.request.dto.ItemRequestReadDtoWithItems;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String HEADERNAME = "X-Sharer-User-Id";

    private final RequestService requestService;

    @PostMapping
    public ItemRequestReadDto saveItemRequest(@RequestHeader(HEADERNAME) Integer userId,
                                              @RequestBody ItemRequestWriteDto itemRequestWriteDto) {
        return requestService.saveRequest(itemRequestWriteDto, userId);
    }

    @GetMapping
    public List<ItemRequestReadDtoWithItems> getRequestsByUserId(@RequestHeader(HEADERNAME) Integer userId) {
        return requestService.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestReadDto> getAllRequests() {
        return requestService.findAllRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestReadDtoWithItems getRequestById(@PathVariable("requestId") Integer requestId) {
        return requestService.getRequestById(requestId);
    }
}
