package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserReadDto;
import ru.practicum.shareit.user.dto.UserWriteDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    private UserReadDto userReadDto;
    private UserWriteDto userWriteDto;

    @BeforeEach
    void setUp() {
        userReadDto = new UserReadDto(1, "john@example.com", "John Doe");
        userWriteDto = new UserWriteDto("john@example.com", "John Doe");
    }

    @Test
    void getUserById_ReturnsUser() throws Exception {
        Mockito.when(userClient.getUserById(1L))
                .thenReturn(ResponseEntity.ok(userReadDto));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userReadDto.getId()))
                .andExpect(jsonPath("$.name").value(userReadDto.getName()))
                .andExpect(jsonPath("$.email").value(userReadDto.getEmail()));
    }

    @Test
    void createUser_ReturnsCreatedUser() throws Exception {
        Mockito.when(userClient.saveUser(any(UserWriteDto.class)))
                .thenReturn(ResponseEntity.ok(userReadDto));

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
        Mockito.when(userClient.updateUser(eq(1L), any(UserWriteDto.class)))
                .thenReturn(ResponseEntity.ok(userReadDto));

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWriteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userReadDto.getId()))
                .andExpect(jsonPath("$.name").value(userReadDto.getName()))
                .andExpect(jsonPath("$.email").value(userReadDto.getEmail()));
    }

    @Test
    void deleteUser_ReturnsOk() throws Exception {
        Mockito.when(userClient.deleteUser(1L))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(userClient).deleteUser(1L);
    }

    @Test
    void createUser_WithInvalidEmail_ReturnsBadRequest() throws Exception {
        // Некорректный email
        userWriteDto = new UserWriteDto("email", "John Doe");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWriteDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Электронная почта должна соответствовать определённому формату !"));
    }

    @Test
    void createUser_WithBlankEmail_ReturnsBadRequest() throws Exception {
        userWriteDto = new UserWriteDto("", "John Doe");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWriteDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Отсутствует электронная почта !"));
    }

    @Test
    void createUser_WithBlankName_ReturnsBadRequest() throws Exception {
        userWriteDto = new UserWriteDto("john@example.com", "");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWriteDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Отсутствует имя пользователя !"));
    }
}
