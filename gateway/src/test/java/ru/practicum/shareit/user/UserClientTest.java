package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserWriteDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserClientTest {

    private RestTemplate restTemplate;
    private UserClient userClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        userClient = new UserClient(restTemplate);
    }

    @Test
    void getUserById_ShouldReturnOkResponse() {
        ResponseEntity<Object> expected = ResponseEntity.ok("user");
        Mockito.when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = userClient.getUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user", response.getBody());
        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(Object.class));
    }

    @Test
    void saveUser_ShouldReturnOkResponse() {
        UserWriteDto dto = new UserWriteDto("john@example.com", "John");
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("created");

        Mockito.when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userClient.saveUser(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("created", response.getBody());
        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(Object.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        UserWriteDto dto = new UserWriteDto("new@example.com", "New Name");
        ResponseEntity<Object> expected = ResponseEntity.ok("updated");

        Mockito.when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = userClient.updateUser(1, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated", response.getBody());
        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.PATCH), any(), eq(Object.class));
    }

    @Test
    void deleteUser_ShouldReturnOk() {
        ResponseEntity<Object> expected = ResponseEntity.ok().build();

        Mockito.when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(expected);

        ResponseEntity<Object> response = userClient.deleteUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(Object.class));
    }
}
