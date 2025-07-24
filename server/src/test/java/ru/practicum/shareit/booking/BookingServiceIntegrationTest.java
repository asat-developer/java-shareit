package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingReadDto;
import ru.practicum.shareit.booking.dto.BookingWriteDto;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceIntegrationTest {

    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Test
    void findByBooker_findByOwner() {
        User user1 = new User();
        user1.setName("Owner");
        user1.setEmail("owner@example.com");

        User user2 = new User();
        user2.setName("Booker");
        user2.setEmail("booker@example.com");

        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        Item item = new Item(savedUser1, null);
        item.setDescription("Wooden");
        item.setName("Table");
        item.setAvailable(true);
        itemRepository.save(item);


        BookingWriteDto dto1 = new BookingWriteDto(LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1);

        bookingService.saveBooking(dto1, 2);
        List<BookingReadDto> result1 = bookingService.getBookingsByUser(savedUser2.getId(), State.FUTURE);
        assertNotNull(result1);
        assertEquals(result1.getFirst().getBooker().getId(), 2);
        assertEquals(result1.getFirst().getItem().getName(), "Table");
        assertEquals(result1.getFirst().getStatus(), Status.WAITING);

        List<BookingReadDto> result2 = bookingService.getBookingsByUser(savedUser2.getId(), State.PAST);
        assertTrue(result2.isEmpty());

        bookingService.updateBooking(1, 1, false);
        List<BookingReadDto> result3 = bookingService.getBookingsByUser(savedUser2.getId(), State.REJECTED);
        assertNotNull(result3);
        assertEquals(result3.getFirst().getBooker().getId(), 2);
        assertEquals(result3.getFirst().getItem().getName(), "Table");
        assertEquals(result3.getFirst().getStatus(), Status.REJECTED);

        BookingWriteDto dto2 = new BookingWriteDto(LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                1);

        /*User user3 = new User();
        user3.setName("Booker2");
        user3.setEmail("booker2@example.com");
        User savedUser3 = userRepository.save(user3);

        bookingService.saveBooking(dto2, 3);

        bookingService.updateBooking(1, 2, true);*/
        List<BookingReadDto> result4 = bookingService.getBookingsByOwner(savedUser1.getId(), State.ALL);
        assertNotNull(result4);
        assertEquals(result4.getFirst().getItem().getName(), "Table");

        List<BookingReadDto> result5 = bookingService.getBookingsByOwner(savedUser1.getId(), State.REJECTED);
        assertNotNull(result4);
        assertEquals(result4.getFirst().getItem().getName(), "Table");

        List<BookingReadDto> result6 = bookingService.getBookingsByOwner(savedUser1.getId(), State.WAITING);
        assertTrue(result6.isEmpty());

        List<BookingReadDto> result7 = bookingService.getBookingsByOwner(savedUser1.getId(), State.PAST);
        assertTrue(result7.isEmpty());

        List<BookingReadDto> result8 = bookingService.getBookingsByOwner(savedUser1.getId(), State.FUTURE);
        assertNotNull(result8);
        assertEquals(result8.getFirst().getItem().getName(), "Table");
    }
}

