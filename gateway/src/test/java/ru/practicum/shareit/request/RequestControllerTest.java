package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    private static final String HEADER_NAME = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestClient requestClient;

    private ItemRequestWriteDto itemRequestWriteDto;

    @BeforeEach
    void setUp() {
        itemRequestWriteDto = new ItemRequestWriteDto();
        itemRequestWriteDto.setDescription("Need a drill");
    }

    @Test
    void saveItemRequest_ShouldReturnOk() throws Exception {
        Mockito.when(requestClient.saveItemRequest(anyLong(), any(ItemRequestWriteDto.class)))
                .thenReturn(ResponseEntity.ok("created"));

        mockMvc.perform(post("/requests")
                        .header(HEADER_NAME, 123L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestWriteDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("created"));

        Mockito.verify(requestClient).saveItemRequest(eq(123L), any(ItemRequestWriteDto.class));
    }

    @Test
    void getRequestsByUserId_ShouldReturnOk() throws Exception {
        Mockito.when(requestClient.getRequestsByUserId(anyLong()))
                .thenReturn(ResponseEntity.ok("userRequests"));

        mockMvc.perform(get("/requests")
                        .header(HEADER_NAME, 123L))
                .andExpect(status().isOk())
                .andExpect(content().string("userRequests"));

        Mockito.verify(requestClient).getRequestsByUserId(eq(123L));
    }

    @Test
    void getAllRequests_ShouldReturnOk() throws Exception {
        Mockito.when(requestClient.getAllRequests())
                .thenReturn(ResponseEntity.ok("allRequests"));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("allRequests"));

        Mockito.verify(requestClient).getAllRequests();
    }

    @Test
    void getRequestById_ShouldReturnOk() throws Exception {
        Mockito.when(requestClient.getRequestById(anyLong()))
                .thenReturn(ResponseEntity.ok("request"));

        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("request"));

        Mockito.verify(requestClient).getRequestById(eq(1L));
    }
}
