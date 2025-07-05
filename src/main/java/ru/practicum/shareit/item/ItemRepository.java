package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item getItemById(Integer id);

    Item saveItem(Item item);

    Item updateItem(Item item, Integer id);

    List<Item> getAllItemsByUser(Integer userId);

    List<Item> searchByText(String text);

    boolean checkItem(Integer id);

    List<Item> getAllItems();
}
