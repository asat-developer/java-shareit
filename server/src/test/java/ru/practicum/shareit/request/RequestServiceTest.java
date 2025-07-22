package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestReadDto;
import ru.practicum.shareit.request.dto.ItemRequestReadDtoWithItems;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestServiceTest {

    private RequestRepository requestRepository;
    private UserRepository userRepository;
    private RequestServiceImpl requestService;

    private User user;

    @BeforeEach
    void setUp() {
        requestRepository = mock(RequestRepository.class);
        userRepository = mock(UserRepository.class);
        requestService = new RequestServiceImpl(requestRepository, userRepository);

        user = new User();
        user.setName("User");
        user.setEmail("user@example.com");
        ReflectionTestUtils.setField(user, "id", 1);
    }

    @Test
    void saveRequest_whenValid_thenSavedAndReturned() {
        ItemRequestWriteDto writeDto = new ItemRequestWriteDto("Need hammer");
        ItemRequest request = new ItemRequest(user, LocalDateTime.now());
        request.setDescription("Need hammer");

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(requestRepository.save(any())).thenAnswer(entity -> {
            ItemRequest saved = entity.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", 1);
            return saved;
        });

        ItemRequestReadDto result = requestService.saveRequest(writeDto, 1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Need hammer", result.getDescription());
        assertEquals(1, result.getRequestorId());
        verify(requestRepository).save(any(ItemRequest.class));
    }

    @Test
    void saveRequest_whenUserNotFound_thenThrowException() {
        Mockito.when(userRepository.findById(99)).thenReturn(Optional.empty());

        ItemRequestWriteDto writeDto = new ItemRequestWriteDto("Test");

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> requestService.saveRequest(writeDto, 99));

        assertEquals("Не существует пользователь, создавшего запрос !", exception.getMessage());
    }

    @Test
    void getRequestsByUserId_whenUserExists_thenReturnList() {
        Mockito.when(userRepository.existsById(1)).thenReturn(true);
        Mockito.when(requestRepository.findByRequestorIdWithItems(1)).thenReturn(Collections.emptyList());

        List<ItemRequestReadDtoWithItems> result = requestService.getRequestsByUserId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getRequestsByUserId_whenUserNotFound_thenThrowException() {
        Mockito.when(userRepository.existsById(42)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> requestService.getRequestsByUserId(42));

        assertEquals("Не существует пользователя, отправившего запрос", exception.getMessage());
    }

    @Test
    void findAllRequests_shouldReturnAllRequests() {
        Mockito.when(requestRepository.findAllByOrderByTimeCreatedDesc()).thenReturn(Collections.emptyList());

        List<ItemRequestReadDto> result = requestService.findAllRequests();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

