package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemDtoMapper {
    public Item itemWriteDtoToItem(ItemWriteDto itemWriteDto, Integer userId) {
        return new Item(null,
                itemWriteDto.getName(),
                itemWriteDto.getDescription(),
                itemWriteDto.getAvailable(),
                userId,
                null);
    }

    public ItemReadDto itemToItemReadDto(Item item) {
        return new ItemReadDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                item.getRequestId());
    }
}
