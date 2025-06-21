package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestReadDto;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

@Slf4j
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
        return itemRequestToItemRequestReadDto(requestRepository.getRequestById(requestId));
    }

    @Override
    public ItemRequestReadDto saveRequest(ItemRequestWriteDto itemRequestWriteDto, Integer userId) {
        if (!userRepository.checkUser(userId)) {
            throw new NotFoundException("Не существует пользователь, создавшего запрос !");
        }
        return itemRequestToItemRequestReadDto(requestRepository
                .saveRequest(itemRequestWriteDtoToItemRequest(itemRequestWriteDto, userId)));
    }

    private ItemRequestReadDto itemRequestToItemRequestReadDto(ItemRequest itemRequest) {
        return new ItemRequestReadDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestorId(),
                itemRequest.getTimeCreated());
    }

    private ItemRequest itemRequestWriteDtoToItemRequest(ItemRequestWriteDto itemRequestWriteDto, Integer userId) {
        return new ItemRequest(null,
                itemRequestWriteDto.getDescription(),
                userId,
                LocalDateTime.now());
    }
}
