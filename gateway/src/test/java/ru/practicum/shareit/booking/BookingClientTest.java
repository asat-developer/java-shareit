package ru.practicum.shareit.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import ru.practicum.shareit.booking.dto.BookingWriteDto;

public class BookingClientTest {


    private RestTemplate restTemplate;
    private BookingClient bookingClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        bookingClient = new BookingClient(restTemplate);
    }

    @Test
    void saveBooking_ShouldReturnOk() {
        BookingWriteDto dto = new BookingWriteDto(null, null, 1);
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("saved");

        Mockito.when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingClient.saveBooking(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("saved", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class));
    }

    @Test
    void updateBooking_ShouldReturnOk() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("updated");

        Mockito.when(restTemplate.exchange(
                eq("/1?approved={approved}"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("approved", true))
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingClient.updateBooking(1L, 1L, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(eq("/1?approved={approved}"), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class), eq(Map.of("approved", true)));
    }

    @Test
    void getBookingByBookingId_ShouldReturnOk() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("booking");

        Mockito.when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingClient.getBookingByBookingId(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("booking", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(eq("/1"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class));
    }

    @Test
    void getBookingsByUser_ShouldReturnOk() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("bookings");

        when(restTemplate.exchange(
                eq("?state={state}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("state", State.ALL))
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingClient.getBookingsByUser(1L, State.ALL);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("bookings", response.getBody());

        verify(restTemplate, times(1)).exchange(eq("?state={state}"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), eq(Map.of("state", State.ALL)));
    }

    @Test
    void getBookingsByOwner_ShouldReturnOk() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ownerBookings");

        Mockito.when(restTemplate.exchange(
                eq("/owner?state={state}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingClient.getBookingsByOwner(1L, State.ALL);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ownerBookings", response.getBody());

        Mockito.verify(restTemplate, times(1)).exchange(eq("/owner?state={state}"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class));
    }
}
