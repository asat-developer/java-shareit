package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestReadDto;
import ru.practicum.shareit.request.dto.ItemRequestReadDtoWithItems;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemRequestReadDto requestReadDto;
    private ItemRequestReadDtoWithItems requestReadDtoWithItems;
    private ItemRequestWriteDto requestWriteDto;

    @BeforeEach
    void setUp() {
        requestWriteDto = new ItemRequestWriteDto("Need a drill");
        requestReadDto = new ItemRequestReadDto(1, "Need a drill", 1, LocalDateTime.now());
        requestReadDtoWithItems = new ItemRequestReadDtoWithItems(1, "Need a drill", 1, LocalDateTime.now(), Collections.emptyList());
    }

    @Test
    void saveItemRequest_shouldReturnSavedDto() throws Exception {
        Mockito.when(requestService.saveRequest(any(), eq(1))).thenReturn(requestReadDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWriteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestReadDto.getId()))
                .andExpect(jsonPath("$.description").value(requestReadDto.getDescription()))
                .andExpect(jsonPath("$.requestorId").value(requestReadDto.getRequestorId()));
    }

    @Test
    void getRequestsByUserId_shouldReturnList() throws Exception {
        Mockito.when(requestService.getRequestsByUserId(1)).thenReturn(List.of(requestReadDtoWithItems));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(requestReadDtoWithItems.getId()))
                .andExpect(jsonPath("$[0].description").value(requestReadDtoWithItems.getDescription()));
    }

    @Test
    void getAllRequests_shouldReturnList() throws Exception {
        Mockito.when(requestService.findAllRequests()).thenReturn(List.of(requestReadDto));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(requestReadDto.getId()))
                .andExpect(jsonPath("$[0].description").value(requestReadDto.getDescription()));
    }

    @Test
    void getRequestById_shouldReturnDto() throws Exception {
        Mockito.when(requestService.getRequestById(1)).thenReturn(requestReadDtoWithItems);

        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestReadDtoWithItems.getId()))
                .andExpect(jsonPath("$.description").value(requestReadDtoWithItems.getDescription()));
    }
}
