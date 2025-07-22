package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private BookingService bookingsService;

    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    private User user;
    private Item item;
    private final Integer userTestId = 1;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingsService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        user = new User();
        user.setName("Test");
        user.setEmail("test@example.com");
        ReflectionTestUtils.setField(user, "id", 1);


        item = new Item();
        item.setName("Drill");
        item.setAvailable(true);
        ReflectionTestUtils.setField(item, "id", 1);
        ReflectionTestUtils.setField(item, "owner", user);
    }

    @Test
    void saveBooking_shouldReturnBookingDto() {
        BookingWriteDto writeDto = new BookingWriteDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1
        );

        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

        BookingReadDto result = bookingsService.saveBooking(writeDto, 1);

        assertNotNull(result);
        assertEquals(result.getItem().getId(), 1);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void saveBooking_whenItemNotFound_shouldThrow() {
        BookingWriteDto writeDto = new BookingWriteDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                999
        );

        Mockito.when(itemRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingsService.saveBooking(writeDto, 1));

        assertEquals("Нет запрашиваемого предмета !", exception.getMessage());
    }

    @Test
    void saveBooking_whenItemUnavailable_shouldThrow() {
        item.setAvailable(false);
        BookingWriteDto writeDto = new BookingWriteDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1
        );

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingsService.saveBooking(writeDto, 1));

        assertEquals("Предмет недоступен для заказа !", exception.getMessage());
    }

    @Test
    void updateBooking_shouldApproveBooking() {
        Booking booking = new Booking(item, user);
        booking.setStatus(Status.WAITING);

        when(userRepository.existsById(1)).thenReturn(true);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        BookingReadDto result = bookingsService.updateBooking(1, 1, true);

        assertEquals(Status.APPROVED, result.getStatus());
    }

    @Test
    void updateBooking_whenUserNotOwner_shouldThrow() {
        User otherUser = new User();
        ReflectionTestUtils.setField(otherUser, "id", 99);

        ReflectionTestUtils.setField(item, "owner", otherUser);
        Booking booking = new Booking(item, user);

        when(userRepository.existsById(1)).thenReturn(true);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingsService.updateBooking(1, 1, true));

        assertEquals("Нарушение прав !", exception.getMessage());
    }

    @Test
    void getBookingByBookingId_shouldReturnDto() {
        Booking booking = new Booking(item, user);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        BookingReadDto result = bookingsService.getBookingByBookingId(1, 1);

        assertNotNull(result);
        assertEquals(result.getBooker().getId(), 1);
    }

    @Test
    void getBookingByBookingId_whenNotOwnerOrBooker_shouldThrow() {
        User otherUser = new User();
        ReflectionTestUtils.setField(otherUser, "id", 99);

        ReflectionTestUtils.setField(item, "owner", otherUser);

        Booking booking = new Booking(item, user);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingsService.getBookingByBookingId(42, 1));

        assertEquals("Нарушение прав !", exception.getMessage());
    }






    //Методы при перечислении параметра State
    @Test
    void getBookingsByUser_whenUserNotFound_thenThrowException() {
        when(userRepository.existsById(userTestId)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingsService.getBookingsByUser(userTestId, State.ALL));
    }

    @Test
    void getBookingsByUser_whenStateAll_thenReturnList() {
        when(userRepository.existsById(userTestId)).thenReturn(true);
        when(bookingRepository.findByBookerId(userTestId)).thenReturn(List.of(mockBooking()));

        List<BookingReadDto> result = bookingsService.getBookingsByUser(userTestId, State.ALL);

        assertEquals(1, result.size());
    }

    @Test
    void getBookingsByUser_whenStateCurrent_thenReturnList() {
        when(userRepository.existsById(userTestId)).thenReturn(true);
        when(bookingRepository.findByBookerIdAndByStartAndEndBetween(userTestId))
                .thenReturn(List.of(mockBooking()));

        List<BookingReadDto> result = bookingsService.getBookingsByUser(userTestId, State.CURRENT);

        assertEquals(1, result.size());
    }

    @Test
    void getBookingsByUser_whenStateWaiting_thenReturnEmptyList() {
        when(userRepository.existsById(userTestId)).thenReturn(true);
        when(bookingRepository.findByBookerIdAndStatusIs(userTestId, Status.WAITING))
                .thenReturn(Collections.emptyList());

        List<BookingReadDto> result = bookingsService.getBookingsByUser(userTestId, State.WAITING);

        assertTrue(result.isEmpty());
    }

    @Test
    void getBookingsByOwner_whenStateRejected_thenReturnList() {
        when(userRepository.existsById(userTestId)).thenReturn(true);
        when(bookingRepository.findByItemOwnerIdAndStatusIs(userTestId, Status.REJECTED))
                .thenReturn(List.of(mockBooking()));

        List<BookingReadDto> result = bookingsService.getBookingsByOwner(userTestId, State.REJECTED);

        assertEquals(1, result.size());
    }

    @Test
    void getBookingsByOwner_whenUserNotFound_thenThrowException() {
        when(userRepository.existsById(userTestId)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> bookingsService.getBookingsByOwner(userTestId, State.ALL));
    }

    private Booking mockBooking() {
        User user1 = new User();
        user1.setName("Tom");
        user1.setEmail("Brown@gmail.com");
        ReflectionTestUtils.setField(user1, "id", 1);

        User user2 = new User();
        user1.setName("Owner");
        user1.setEmail("Owner@gmail.com");
        ReflectionTestUtils.setField(user2, "id", 2);

        Item item = new Item(user2, null);
        item.setDescription("Wooden");
        item.setName("Table");
        item.setAvailable(true);
        ReflectionTestUtils.setField(item, "id", 1);

        Booking booking = new Booking(item, user1);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(Status.WAITING);
        return booking;
    }
}