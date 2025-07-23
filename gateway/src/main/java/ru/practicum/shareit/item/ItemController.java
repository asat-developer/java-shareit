package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.ItemClient;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String HEADERNAME = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(HEADERNAME) Integer userId,
                                                          @PathVariable("itemId") Integer itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItems() {
        return itemClient.getAllItems();
    }

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestHeader(HEADERNAME) Integer userId,
                                @RequestBody @Valid ItemWriteDto itemWriteDto) {
        return itemClient.saveItem(userId, itemWriteDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(HEADERNAME) Integer userId,
                                  @RequestBody ItemWriteDto itemWriteDto,
                                  @PathVariable("itemId") Integer itemId) {
        return itemClient.updateItem(userId, itemId, itemWriteDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUser(@RequestHeader(HEADERNAME) Integer userId) {
        return itemClient.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByText(@RequestParam(defaultValue = "") String text) {
        return itemClient.searchByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@RequestHeader(HEADERNAME) Integer userId,
                                      @RequestBody @Valid CommentWriteDto commentWriteDto,
                                      @PathVariable("itemId") Integer itemId) {
        return itemClient.saveComment(userId, itemId, commentWriteDto);
    }
}
