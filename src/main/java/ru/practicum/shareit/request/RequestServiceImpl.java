package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.dto.ItemRequestReadDto;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestReadDto getRequestById(Integer requestId) {
        if (!requestRepository.existsById(requestId)) {
            throw new NotFoundException("Нет запроса с таким id");
        }
        return ItemRequestDtoMapper.itemRequestToItemRequestReadDto(requestRepository.findById(requestId).get());
    }

    @Override
    public ItemRequestReadDto saveRequest(ItemRequestWriteDto itemRequestWriteDto, Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Не существует пользователь, создавшего запрос !");
        }
        User user = userRepository.findById(userId).get();
        ItemRequest request = ItemRequestDtoMapper.itemRequestWriteDtoToItemRequest(itemRequestWriteDto, user);
        return ItemRequestDtoMapper.itemRequestToItemRequestReadDto(requestRepository.save(request));
    }
}
