package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemDtoMapper {
    public static Item itemWriteDtoToItem(ItemWriteDto itemWriteDto, Integer userId) {
        return new Item(null,
                itemWriteDto.getName(),
                itemWriteDto.getDescription(),
                itemWriteDto.getAvailable(),
                userId,
                null);
    }

    public static ItemReadDto itemToItemReadDto(Item item) {
        return new ItemReadDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                item.getRequestId());
    }
}
