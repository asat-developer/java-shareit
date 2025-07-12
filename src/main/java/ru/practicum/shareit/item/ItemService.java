package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemReadDtoWithBookingsAndComments getItemById(Integer id, Integer userId);

    ItemReadDto saveItem(ItemWriteDto itemWriteDto, Integer userId);

    ItemReadDto updateItem(ItemWriteDto itemWriteDto, Integer userId, Integer itemId);

    List<ItemReadDto> getAllItemsByUser(Integer userId);

    List<ItemReadDto> searchByText(String text);

    List<ItemReadDto> getAllItems();

    CommentReadDto saveComment(CommentWriteDto commentWriteDto, Integer userId, Integer itemId);
}
