package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private static final String HEADERNAME = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId,
                                                          @PathVariable("itemId") @Positive @NotNull Integer itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItems() {
        return itemClient.getAllItems();
    }

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId,
                                @RequestBody @Valid ItemWriteDto itemWriteDto) {
        return itemClient.saveItem(userId, itemWriteDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId,
                                  @RequestBody @Valid ItemWriteDto itemWriteDto,
                                  @PathVariable("itemId") @Positive @NotNull Integer itemId) {
        return itemClient.updateItem(userId, itemId, itemWriteDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUser(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId) {
        return itemClient.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByText(@RequestParam(defaultValue = "") String text) {
        return itemClient.searchByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@RequestHeader(HEADERNAME) @Positive @NotNull Integer userId,
                                      @RequestBody @Valid CommentWriteDto commentWriteDto,
                                      @PathVariable("itemId") @Positive @NotNull Integer itemId) {
        return itemClient.saveComment(userId, itemId, commentWriteDto);
    }
}
