package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingWriteDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    private static final String HEADER_NAME = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    private BookingWriteDto bookingWriteDto;

    @BeforeEach
    void setUp() {
        bookingWriteDto = new BookingWriteDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                1
        );
    }

    @Test
    void saveBooking_ShouldReturnOk() throws Exception {
        Mockito.when(bookingClient.saveBooking(anyLong(), any(BookingWriteDto.class)))
                .thenReturn(ResponseEntity.ok("created"));

        mockMvc.perform(post("/bookings")
                        .header(HEADER_NAME, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingWriteDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("created"));

        Mockito.verify(bookingClient).saveBooking(eq(1L), any(BookingWriteDto.class));
    }

    @Test
    void updateBooking_ShouldReturnOk() throws Exception {
        Mockito.when(bookingClient.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok("updated"));

        mockMvc.perform(patch("/bookings/42")
                        .header(HEADER_NAME, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string("updated"));

        Mockito.verify(bookingClient).updateBooking(1L, 42L, true);
    }

    @Test
    void getBookingByBookingId_ShouldReturnOk() throws Exception {
        Mockito.when(bookingClient.getBookingByBookingId(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok("booking"));

        mockMvc.perform(get("/bookings/7")
                        .header(HEADER_NAME, 2L))
                .andExpect(status().isOk())
                .andExpect(content().string("booking"));

        Mockito.verify(bookingClient).getBookingByBookingId(2L, 7L);
    }

    @Test
    void getBookingsByUser_ShouldReturnOk() throws Exception {
        Mockito.when(bookingClient.getBookingsByUser(anyLong(), eq(State.ALL)))
                .thenReturn(ResponseEntity.ok("bookings"));

        mockMvc.perform(get("/bookings")
                        .header(HEADER_NAME, 3L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(content().string("bookings"));

        Mockito.verify(bookingClient).getBookingsByUser(3L, State.ALL);
    }

    @Test
    void getBookingsByOwner_ShouldReturnOk() throws Exception {
        Mockito.when(bookingClient.getBookingsByOwner(anyLong(), eq(State.ALL)))
                .thenReturn(ResponseEntity.ok("ownerBookings"));

        mockMvc.perform(get("/bookings/owner")
                        .header(HEADER_NAME, 4L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(content().string("ownerBookings"));

        Mockito.verify(bookingClient).getBookingsByOwner(4L, State.ALL);
    }

    @Test
    void getBookingByBookingId_ShouldReturnBadRequest_WhenBookingIdIsNegative() throws Exception {
        mockMvc.perform(get("/bookings/0")
                        .header(HEADER_NAME, 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Передаваемые параметры должны быть больше 0"));
    }

    @Test
    void updateBooking_ShouldReturnBadRequest_WhenApprovedIsInvalid() throws Exception {
        mockMvc.perform(patch("/bookings/10")
                        .header(HEADER_NAME, 1)
                        .param("approved", "notBoolean"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Параметр 'approved' должен быть типа Boolean"));
    }

    @Test
    void getBookingsByUser_ShouldReturnBadRequest_WhenStateIsInvalid() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header(HEADER_NAME, 1)
                        .param("state", "invalidState"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Некорректный формат state: invalidState"));
    }
}

