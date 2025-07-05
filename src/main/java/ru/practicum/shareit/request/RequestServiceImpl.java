package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.dto.ItemRequestReadDto;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestReadDto getRequestById(Integer requestId) {
        if (!requestRepository.checkRequest(requestId)) {
            throw new NotFoundException("Нет запроса с таким id");
        }
        return ItemRequestDtoMapper.itemRequestToItemRequestReadDto(requestRepository.getRequestById(requestId));
    }

    @Override
    public ItemRequestReadDto saveRequest(ItemRequestWriteDto itemRequestWriteDto, Integer userId) {
        if (!userRepository.checkUser(userId)) {
            throw new NotFoundException("Не существует пользователь, создавшего запрос !");
        }
        return ItemRequestDtoMapper.itemRequestToItemRequestReadDto(requestRepository
                .saveRequest(ItemRequestDtoMapper.itemRequestWriteDtoToItemRequest(itemRequestWriteDto, userId)));
    }
}
