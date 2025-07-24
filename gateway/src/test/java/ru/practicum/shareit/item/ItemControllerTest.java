package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentWriteDto;
import ru.practicum.shareit.item.dto.ItemWriteDto;
import ru.practicum.shareit.item.ItemClient;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    private ItemWriteDto itemWriteDto;
    private CommentWriteDto commentWriteDto;

    private static final String HEADER_NAME = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        itemWriteDto = new ItemWriteDto(
                "Drill",
                "Powerful drill",
                true,
                null,
                null
        );

        commentWriteDto = new CommentWriteDto("Great item!");
    }

    @Test
    void getItemById_ShouldReturnOkResponse() throws Exception {
        Mockito.when(itemClient.getItemById(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok("item"));

        mockMvc.perform(get("/items/1")
                        .header(HEADER_NAME, 123))
                .andExpect(status().isOk())
                .andExpect(content().string("item"));

        Mockito.verify(itemClient).getItemById(123, 1);
    }

    @Test
    void getAllItems_ShouldReturnOkResponse() throws Exception {
        Mockito.when(itemClient.getAllItems()).thenReturn(ResponseEntity.ok("allItems"));

        mockMvc.perform(get("/items/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("allItems"));

        Mockito.verify(itemClient).getAllItems();
    }

    @Test
    void saveItem_ShouldReturnOkResponse() throws Exception {
        Mockito.when(itemClient.saveItem(anyLong(), any(ItemWriteDto.class)))
                .thenReturn(ResponseEntity.ok("saved"));

        mockMvc.perform(post("/items")
                        .header(HEADER_NAME, 123L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemWriteDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("saved"));

        Mockito.verify(itemClient).saveItem(eq(123L), any(ItemWriteDto.class));
    }

    @Test
    void updateItem_ShouldReturnOkResponse() throws Exception {
        Mockito.when(itemClient.updateItem(anyLong(), anyLong(), any(ItemWriteDto.class)))
                .thenReturn(ResponseEntity.ok("updated"));

        mockMvc.perform(patch("/items/1")
                        .header(HEADER_NAME, 123L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemWriteDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("updated"));

        Mockito.verify(itemClient).updateItem(eq(123L), eq(1L), any(ItemWriteDto.class));
    }

    @Test
    void getAllItemsByUser_ShouldReturnOkResponse() throws Exception {
        Mockito.when(itemClient.getAllItemsByUser(anyLong())).thenReturn(ResponseEntity.ok("userItems"));

        mockMvc.perform(get("/items")
                        .header(HEADER_NAME, 123))
                .andExpect(status().isOk())
                .andExpect(content().string("userItems"));

        Mockito.verify(itemClient).getAllItemsByUser(123);
    }

    @Test
    void searchByText_ShouldReturnOkResponse() throws Exception {
        Mockito.when(itemClient.searchByText(anyString())).thenReturn(ResponseEntity.ok("searchResults"));

        mockMvc.perform(get("/items/search")
                        .param("text", "drill"))
                .andExpect(status().isOk())
                .andExpect(content().string("searchResults"));

        Mockito.verify(itemClient).searchByText("drill");
    }

    @Test
    void saveComment_ShouldReturnOkResponse() throws Exception {
        when(itemClient.saveComment(anyLong(), anyLong(), any(CommentWriteDto.class)))
                .thenReturn(ResponseEntity.ok("commentSaved"));

        mockMvc.perform(post("/items/1/comment")
                        .header(HEADER_NAME, 123L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentWriteDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("commentSaved"));

        verify(itemClient).saveComment(eq(123L), eq(1L), any(CommentWriteDto.class));
    }
}
