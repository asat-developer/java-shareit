package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingReadDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private static final String HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void getItemById_shouldReturnItem() throws Exception {
        int userId = 1;
        int itemId = 100;

        BookingReadDto lastBooking = new BookingReadDto(10, null, null, null, null, null);
        BookingReadDto nextBooking = new BookingReadDto(11, null, null, null, null, null);
        CommentReadDto comment1 = new CommentReadDto(1, "Text1", 100, null, null);
        CommentReadDto comment2 = new CommentReadDto(2, "Text2", 100, null, null);

        ItemReadDtoWithBookingsAndComments response = new ItemReadDtoWithBookingsAndComments(
                itemId, "Дрель",
                "Описание",
                true,
                userId,
                null,
                List.of(comment1, comment2),
                lastBooking,
                nextBooking
        );

        Mockito.when(itemService.getItemById(itemId, userId)).thenReturn(response);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header(HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value("Дрель"))
                .andExpect(jsonPath("$.description").value("Описание"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.ownerId").value(userId))
                .andExpect(jsonPath("$.comments[0].text").value("Text1"))
                .andExpect(jsonPath("$.comments[1].text").value("Text2"))
                .andExpect(jsonPath("$.lastBooking.id").value(10))
                .andExpect(jsonPath("$.nextBooking.id").value(11));
    }

    @Test
    void getAllItems_shouldReturnList() throws Exception {
        ItemReadDto item1 = new ItemReadDto(1, "Item1", "Desc1", true, null, null);
        ItemReadDto item2 = new ItemReadDto(2, "Item2", "Desc2", false, null, null);

        Mockito.when(itemService.getAllItems()).thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/items/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Item1"))
                .andExpect(jsonPath("$[1].available").value(false));
    }

    @Test
    void saveItem_shouldReturnSavedItem() throws Exception {
        int userId = 1;

        ItemWriteDto writeDto = new ItemWriteDto("New item", "Desc", true, null, null);
        ItemReadDto response = new ItemReadDto(101, "New item", "Desc", true, userId, null);

        Mockito.when(itemService.saveItem(any(ItemWriteDto.class), eq(userId))).thenReturn(response);

        mockMvc.perform(post("/items")
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.name").value("New item"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        Integer userId = 1;
        Integer itemId = 101;

        ItemWriteDto updateDto = new ItemWriteDto("Updated", "Updated desc", false, null, null);
        ItemReadDto response = new ItemReadDto(itemId, "Updated", "Updated desc", false, userId, null);

        Mockito.when(itemService.updateItem(any(ItemWriteDto.class), eq(userId), eq(itemId))).thenReturn(response);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void getAllItemsByUser_shouldReturnList() throws Exception {
        Integer userId = 1;

        ItemReadDto item1 = new ItemReadDto(1, "Item1", "Desc1", true, userId, null);
        ItemReadDto item2 = new ItemReadDto(2, "Item2", "Desc2", false, userId, null);

        Mockito.when(itemService.getAllItemsByUser(userId)).thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/items")
                        .header(HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name").value("Item2"));
    }

    @Test
    void searchByText_shouldReturnResults() throws Exception {
        ItemReadDto item = new ItemReadDto(1, "Дрель", "Мощная дрель", true, null, null);

        Mockito.when(itemService.searchByText("дрель")).thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .param("text", "дрель"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Дрель"));
    }

    @Test
    void saveComment_shouldReturnComment() throws Exception {
        Integer userId = 1;
        Integer itemId = 100;

        CommentWriteDto writeDto = new CommentWriteDto("Отличная вещь!");
        CommentReadDto response = new CommentReadDto(1, "Отличная вещь!", 100, "user1", null);

        Mockito.when(itemService.saveComment(any(CommentWriteDto.class), eq(userId), eq(itemId))).thenReturn(response);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Отличная вещь!"))
                .andExpect(jsonPath("$.authorName").value("user1"));
    }

    @Test
    void getAllItemsByUser_whenUserNotFound_thenThrowNotFound() throws Exception {
          Mockito.when(itemService.getAllItemsByUser(anyInt()))
                  .thenThrow(new NotFoundException("Нет такого пользователя"));

        mockMvc.perform(get("/items")
                        .header(HEADER, 2))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItemByNotOwner_thenThrowValidation() throws Exception {
        Integer userId = 1;
        Integer itemId = 101;

        ItemWriteDto updateDto = new ItemWriteDto("Updated", "Updated desc", false, null, null);
        ItemReadDto response = new ItemReadDto(itemId, "Updated", "Updated desc", false, userId, null);

        Mockito.when(itemService.updateItem(any(ItemWriteDto.class), eq(userId), eq(itemId)))
                .thenThrow(new ValidationException("Нарушение прав !", HttpStatus.BAD_REQUEST));

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header(HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }
}
