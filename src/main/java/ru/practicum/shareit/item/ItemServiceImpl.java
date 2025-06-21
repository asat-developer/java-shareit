package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemReadDto;
import ru.practicum.shareit.item.dto.ItemWriteDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemDtoMapper itemDtoMapper;
    private final UserRepository userRepository;

    @Override
    public ItemReadDto getItemById(Integer id) {
        if (!itemRepository.checkItem(id)) {
            log.info("Предмета с id = {} нет", id);
            throw new NotFoundException("Предмета с таким id нет");
        }
        return itemDtoMapper.itemToItemReadDto(itemRepository.getItemById(id));
    }

    @Override
    public ItemReadDto saveItem(ItemWriteDto itemWriteDto, Integer userId) {
        if (!userRepository.checkUser(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        Item item = itemDtoMapper.itemWriteDtoToItem(itemWriteDto, userId);
        return itemDtoMapper.itemToItemReadDto(itemRepository.saveItem(item));
    }

    @Override
    public ItemReadDto updateItem(ItemWriteDto itemWriteDto, Integer userId, Integer itemId) {
        if (!userRepository.checkUser(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        Item item = itemDtoMapper.itemWriteDtoToItem(itemWriteDto, userId);
        if (!itemRepository.getItemById(itemId).getOwnerId().equals(userId)) {
            throw new ValidationException("Нарушение прав !");
        }
        return itemDtoMapper.itemToItemReadDto(itemRepository.updateItem(item, itemId));
    }

    @Override
    public List<ItemReadDto> getAllItemsByUser(Integer userId) {
        if (!userRepository.checkUser(userId)) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return itemRepository.getAllItemsByUser(userId).stream()
                .map(item -> itemDtoMapper.itemToItemReadDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemReadDto> searchByText(String text) {
        if ("".equals(text)) {
            return new ArrayList<>();
        }
        return itemRepository.searchByText(text).stream()
                .map(item -> itemDtoMapper.itemToItemReadDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemReadDto> getAllItems() {
        return itemRepository.getAllItems().stream()
                .map(item -> itemDtoMapper.itemToItemReadDto(item))
                .collect(Collectors.toList());
    }
}
