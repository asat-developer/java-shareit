package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemReadDto;
import ru.practicum.shareit.item.dto.ItemWriteDto;

import java.util.List;

public interface ItemService {
    ItemReadDto getItemById(Integer id);

    ItemReadDto saveItem(ItemWriteDto itemWriteDto, Integer userId);

    ItemReadDto updateItem(ItemWriteDto itemWriteDto, Integer userId, Integer itemId);

    List<ItemReadDto> getAllItemsByUser(Integer userId);

    List<ItemReadDto> searchByText(String text);

    List<ItemReadDto> getAllItems();
}
