package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private final Integer userId = 1;

    @Test
    void saveBooking_shouldReturnBookingDto() throws Exception {
        BookingWriteDto writeDto = new BookingWriteDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1
        );

        BookingReadDto readDto = new BookingReadDto(
                1,
                writeDto.getStart(),
                writeDto.getEnd(),
                new ItemDtoForBooking(1, "Item name"),
                new BookerDtoForBooking(userId),
                Status.WAITING
        );

        Mockito.when(bookingService.saveBooking(any(BookingWriteDto.class), eq(userId)))
                .thenReturn(readDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(readDto.getId()))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void updateBooking_shouldReturnUpdatedDto() throws Exception {
        BookingReadDto updatedDto = new BookingReadDto(
                1,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                new ItemDtoForBooking(1, "Item name"),
                new BookerDtoForBooking(userId),
                Status.APPROVED
        );

        Mockito.when(bookingService.updateBooking(userId, 1, true)).thenReturn(updatedDto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBookingById_shouldReturnDto() throws Exception {
        BookingReadDto dto = new BookingReadDto(
                1,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                new ItemDtoForBooking(1, "Item name"),
                new BookerDtoForBooking(userId),
                Status.WAITING
        );

        Mockito.when(bookingService.getBookingByBookingId(userId, 1)).thenReturn(dto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void getBookingsByUser_shouldReturnList() throws Exception {
        List<BookingReadDto> bookings = List.of();

        Mockito.when(bookingService.getBookingsByUser(userId, State.ALL)).thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getBookingsByOwner_shouldReturnList() throws Exception {
        List<BookingReadDto> bookings = List.of();

        Mockito.when(bookingService.getBookingsByOwner(userId, State.ALL)).thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}

