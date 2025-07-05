package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item getItemById(Integer id) {
        return items.get(id);
    }

    @Override
    public Item saveItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item newItem, Integer id) {
        Item item = items.get(id);
        if (newItem.getName() != null) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            item.setAvailable(newItem.getAvailable());
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getAllItemsByUser(Integer userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchByText(String text) {
        return items.values().stream()
                .filter(item -> {
                    return item.getAvailable()
                            && (item.getName().toUpperCase().contains(text)
                            || item.getDescription().toUpperCase().contains(text));
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkItem(Integer id) {
        return items.containsKey(id);
    }

    @Override
    public List<Item> getAllItems() {
        return List.copyOf(items.values());
    }

    private Integer getNextId() {
        return items.keySet().stream()
                .max((o1, o2) -> Integer.compare(o1, o2))
                .orElse(0) + 1;
    }
}
