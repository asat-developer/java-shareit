package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RequestClientTest {

    private RestTemplate restTemplate;
    private RequestClient requestClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        requestClient = new RequestClient(restTemplate);
    }

    @Test
    void saveItemRequest_ShouldReturnOkResponse() {
        ItemRequestWriteDto dto = new ItemRequestWriteDto("Need a drill");
        ResponseEntity<Object> expected = ResponseEntity.ok("created");

        Mockito.when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = requestClient.saveItemRequest(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("created", response.getBody());
        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class));
    }

    @Test
    void getRequestsByUserId_ShouldReturnOkResponse() {
        ResponseEntity<Object> expected = ResponseEntity.ok("userRequests");

        Mockito.when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = requestClient.getRequestsByUserId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("userRequests", response.getBody());
        Mockito.verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void getAllRequests_ShouldReturnOkResponse() {
        ResponseEntity<Object> expected = ResponseEntity.ok("allRequests");

        Mockito.when(restTemplate.exchange(
                eq("/all"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = requestClient.getAllRequests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("allRequests", response.getBody());
        Mockito.verify(restTemplate).exchange(eq("/all"), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void getRequestById_ShouldReturnOkResponse() {
        ResponseEntity<Object> expected = ResponseEntity.ok("singleRequest");

        Mockito.when(restTemplate.exchange(
                eq("/5"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = requestClient.getRequestById(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("singleRequest", response.getBody());
        Mockito.verify(restTemplate).exchange(eq("/5"), eq(HttpMethod.GET), any(), eq(Object.class));
    }
}

