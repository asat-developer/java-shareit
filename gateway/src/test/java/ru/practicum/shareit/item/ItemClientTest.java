package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentWriteDto;
import ru.practicum.shareit.item.dto.ItemWriteDto;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ItemClientTest {

    private RestTemplate restTemplate;
    private ItemClient itemClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        itemClient = new ItemClient(restTemplate);
    }

    @Test
    void getItemById_ShouldReturnOkResponse() {
        ResponseEntity<Object> expected = ResponseEntity.ok("item");
        Mockito.when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = itemClient.getItemById(123L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("item", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void getAllItems_ShouldReturnOkResponse() {
        ResponseEntity<Object> expected = ResponseEntity.ok("allItems");
        Mockito.when(restTemplate.exchange(
                eq("/all"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = itemClient.getAllItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("allItems", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void saveItem_ShouldReturnOkResponse() {
        ItemWriteDto dto = new ItemWriteDto("Drill", "Powerful drill", true, null, null);
        ResponseEntity<Object> expected = ResponseEntity.ok("created");
        Mockito.when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = itemClient.saveItem(123L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("created", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class));
    }

    @Test
    void updateItem_ShouldReturnOkResponse() {
        ItemWriteDto dto = new ItemWriteDto("Drill", "Powerful drill", true, null, null);
        ResponseEntity<Object> expected = ResponseEntity.ok("updated");
        Mockito.when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = itemClient.updateItem(123L, 1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class));
    }

    @Test
    void getAllItemsByUser_ShouldReturnOkResponse() {
        ResponseEntity<Object> expected = ResponseEntity.ok("userItems");
        Mockito.when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = itemClient.getAllItemsByUser(123L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("userItems", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void searchByText_ShouldReturnOkResponse() {
        String text = "drill";
        ResponseEntity<Object> expected = ResponseEntity.ok("searchResults");
        Mockito.when(restTemplate.exchange(
                eq("/search?text={text}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("text", text)))
        ).thenReturn(expected);

        ResponseEntity<Object> response = itemClient.searchByText(text);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("searchResults", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(anyString(),
                eq(HttpMethod.GET), any(),
                eq(Object.class),
                eq(Map.of("text", text)));
    }

    @Test
    void saveComment_ShouldReturnOkResponse() {
        CommentWriteDto commentDto = new CommentWriteDto("Nice item");
        ResponseEntity<Object> expected = ResponseEntity.ok("commentSaved");
        Mockito.when(restTemplate.exchange(
                eq("/1/comment"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = itemClient.saveComment(123L, 1L, commentDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("commentSaved", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class));
    }
}

