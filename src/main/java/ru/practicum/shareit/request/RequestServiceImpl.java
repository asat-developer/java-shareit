package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.dto.ItemRequestReadDto;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ItemRequestReadDto getRequestById(Integer requestId) {
        ItemRequest request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Нет запроса с таким id"));
        return ItemRequestDtoMapper.itemRequestToItemRequestReadDto(request);
    }

    @Override
    public ItemRequestReadDto saveRequest(ItemRequestWriteDto itemRequestWriteDto, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Не существует пользователь, создавшего запрос !"));
        ItemRequest request = ItemRequestDtoMapper.itemRequestWriteDtoToItemRequest(itemRequestWriteDto, user);
        return ItemRequestDtoMapper.itemRequestToItemRequestReadDto(requestRepository.save(request));
    }
}
