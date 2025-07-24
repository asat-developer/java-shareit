package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private static final String HEADERNAME = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> saveItemRequest(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId,
                                                  @RequestBody @Valid ItemRequestWriteDto itemRequestWriteDto) {
        return requestClient.saveItemRequest(userId, itemRequestWriteDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUserId(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId) {
        return requestClient.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests() {
        return requestClient.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable("requestId") @Positive @NotNull Integer requestId) {
        return requestClient.getRequestById(requestId);
    }
}
