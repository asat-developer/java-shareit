package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemReadDto;
import ru.practicum.shareit.item.dto.ItemWriteDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemReadDto getItemById(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/all")
    public List<ItemReadDto> getAllItems() {
        return itemService.getAllItems();
    }

    @PostMapping
    public ItemReadDto saveItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                @RequestBody @Valid ItemWriteDto itemWriteDto) {
        return itemService.saveItem(itemWriteDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemReadDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @RequestBody ItemWriteDto itemWriteDto,
                                  @PathVariable("itemId") Integer itemId) {
        return itemService.updateItem(itemWriteDto, userId, itemId);
    }

    @GetMapping
    public List<ItemReadDto> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemReadDto> searchByText(@RequestParam(defaultValue = "") String text) {
        return itemService.searchByText(text);
    }
}
