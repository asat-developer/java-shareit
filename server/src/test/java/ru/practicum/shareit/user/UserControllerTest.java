package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserReadDto;
import ru.practicum.shareit.user.dto.UserWriteDto;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserReadDto userReadDto;
    private UserWriteDto userWriteDto;

    @BeforeEach
    void setUp() {
        userReadDto = new UserReadDto(1, "John Doe", "john@example.com");
        userWriteDto = new UserWriteDto("John Doe", "john@example.com");
    }

    @Test
    void getUserById_ReturnsUser() throws Exception {
        Mockito.when(userService.getUserById(1)).thenReturn(userReadDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userReadDto.getId()))
                .andExpect(jsonPath("$.name").value(userReadDto.getName()))
                .andExpect(jsonPath("$.email").value(userReadDto.getEmail()));
    }

    @Test
    void saveUser_ReturnsSavedUser() throws Exception {
        Mockito.when(userService.saveUser(any(UserWriteDto.class))).thenReturn(userReadDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWriteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userReadDto.getId()))
                .andExpect(jsonPath("$.name").value(userReadDto.getName()))
                .andExpect(jsonPath("$.email").value(userReadDto.getEmail()));
    }

    @Test
    void updateUser_ReturnsUpdatedUser() throws Exception {
        Mockito.when(userService.updateUser(any(UserWriteDto.class), eq(1))).thenReturn(userReadDto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWriteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userReadDto.getId()))
                .andExpect(jsonPath("$.name").value(userReadDto.getName()))
                .andExpect(jsonPath("$.email").value(userReadDto.getEmail()));
    }

    @Test
    void deleteUser_ReturnsNoContent() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(userService, times(1)).deleteUser(1);
    }
}
