package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserReadDto;
import ru.practicum.shareit.user.dto.UserWriteDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void getUserById_whenUserExists_thenReturnsUserReadDto() {
        User user = new User();
        user.setEmail("john@example.com");
        user.setName("John");
        ReflectionTestUtils.setField(user, "id", 1);

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserReadDto result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getUserById_whenUserNotFound_thenThrowsNotFoundException() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    void saveUser_whenEmailNotExists_thenSavesUser() {
        UserWriteDto dto = new UserWriteDto("john@example.com", "John");
        Mockito.when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            ReflectionTestUtils.setField(u, "id", 1);
            return u;
        });

        UserReadDto result = userService.saveUser(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getEmail(), result.getEmail());
    }

    @Test
    void saveUser_whenEmailExists_thenThrowsValidationException() {
        UserWriteDto dto = new UserWriteDto("john@example.com", "John");
        Mockito.when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> userService.saveUser(dto));
        assertEquals("Пользователь с таким email уже есть", ex.getMessage());
    }

    @Test
    void updateUser_whenUserExistsAndEmailNotExists_thenUpdatesUser() {
        Integer id = 1;
        UserWriteDto dto = new UserWriteDto("newemail@example.com", "New Name");

        User existingUser = new User();
        existingUser.setEmail("oldemail@example.com");
        existingUser.setName("Old Name");
        ReflectionTestUtils.setField(existingUser, "id", id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        UserReadDto result = userService.updateUser(dto, id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_whenUserNotFound_thenThrowsNotFoundException() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(new UserWriteDto("a@b.com", "Name"), 1));
    }

    @Test
    void updateUser_whenEmailAlreadyExists_thenThrowsValidationException() {
        Integer id = 1;
        UserWriteDto dto = new UserWriteDto("exists@example.com", "Name");

        User existingUser = new User();
        existingUser.setEmail("oldemail@example.com");
        existingUser.setName("Old Name");
        ReflectionTestUtils.setField(existingUser, "id", id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> userService.updateUser(dto, id));
        assertEquals("Пользователь с таким email уже есть", ex.getMessage());
    }

    @Test
    void deleteUser_whenUserExists_thenDeletesUser() {
        Integer id = 1;
        Mockito.when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        Mockito.verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteUser_whenUserNotFound_thenThrowsNotFoundException() {
        Mockito.when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1));
    }
}


