package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestReadDto saveRequest(ItemRequestWriteDto itemRequestWriteDto, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Не существует пользователь, создавшего запрос !"));
        ItemRequest request = ItemRequestDtoMapper.itemRequestWriteDtoToItemRequest(itemRequestWriteDto, user);
        return ItemRequestDtoMapper.itemRequestToItemRequestReadDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestReadDtoWithItems> getRequestsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Не существует пользователя, отправившего запрос");
        }
        List<RequestWithItems> rawResult = requestRepository.findByRequestorIdWithItems(userId);
        return ItemRequestDtoMapper.rawResponseToDto(rawResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestReadDto> findAllRequests() {
        return requestRepository.findAllByOrderByTimeCreatedDesc().stream()
                .map(entity -> ItemRequestDtoMapper.itemRequestToItemRequestReadDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestReadDtoWithItems getRequestById(Integer requestId) {
        List<RequestWithItems> list = new ArrayList<>(List.of(requestRepository.findByIdWithItems(requestId)));
        return ItemRequestDtoMapper.rawResponseToDto(list).getFirst();
    }
}
