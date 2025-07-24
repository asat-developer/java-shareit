package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String HEADERNAME = "X-Sharer-User-Id";

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemReadDtoWithBookingsAndComments getItemById(@RequestHeader(HEADERNAME) Integer userId,
                                                          @PathVariable("itemId") Integer itemId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/all")
    public List<ItemReadDto> getAllItems() {
        return itemService.getAllItems();
    }

    @PostMapping
    public ItemReadDto saveItem(@RequestHeader(HEADERNAME) Integer userId,
                                @RequestBody ItemWriteDto itemWriteDto) {
        return itemService.saveItem(itemWriteDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemReadDto updateItem(@RequestHeader(HEADERNAME) Integer userId,
                                  @RequestBody ItemWriteDto itemWriteDto,
                                  @PathVariable("itemId") Integer itemId) {
        return itemService.updateItem(itemWriteDto, userId, itemId);
    }

    @GetMapping
    public List<ItemReadDto> getAllItemsByUser(@RequestHeader(HEADERNAME) Integer userId) {
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemReadDto> searchByText(@RequestParam(defaultValue = "") String text) {
        return itemService.searchByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentReadDto saveComment(@RequestHeader(HEADERNAME) Integer userId,
                                   @RequestBody CommentWriteDto commentWriteDto,
                                   @PathVariable("itemId") Integer itemId) {
        return itemService.saveComment(commentWriteDto, userId, itemId);
    }
}
