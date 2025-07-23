package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private RequestRepository requestRepository;
    private CommentRepository commentRepository;
    private BookingRepository bookingRepository;

    private ItemService itemService;

    private User user;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        requestRepository = mock(RequestRepository.class);
        commentRepository = mock(CommentRepository.class);
        bookingRepository = mock(BookingRepository.class);
        itemService = new ItemServiceImpl(itemRepository,
                userRepository,
                requestRepository,
                commentRepository,
                bookingRepository);

        user = mock(User.class);
        Mockito.when(user.getId()).thenReturn(1);

        request = mock(ItemRequest.class);
        Mockito.when(request.getId()).thenReturn(10);
    }

    @Test
    void saveItem_whenValid_thenReturnSavedItemDto() {
        // Данные для сохранения
        ItemWriteDto itemWriteDto = new ItemWriteDto("Item name", "Desc", true, null, 10);

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(requestRepository.findById(10)).thenReturn(Optional.of(request));

        Mockito.when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item item = invocation.getArgument(0);
            ReflectionTestUtils.setField(item, "id", 100);
            return item;
        });

        ItemReadDto savedDto = itemService.saveItem(itemWriteDto, 1);

        assertNotNull(savedDto);
        assertEquals(100, savedDto.getId());
        assertEquals(itemWriteDto.getName(), savedDto.getName());
        assertEquals(itemWriteDto.getDescription(), savedDto.getDescription());
    }


    @Test
    void updateItem_whenUserNotFound_thenThrowNotFound() {
        Mockito.when(userRepository.existsById(1)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(new ItemWriteDto("Item name",
                                "Desc",
                                true,
                                null,
                                10),
                        1,
                        100));
        assertEquals("Такого пользователя не существует", ex.getMessage());
    }

    @Test
    void updateItem_whenItemNotFound_thenThrowNotFound() {
        Mockito.when(userRepository.existsById(1)).thenReturn(true);
        Mockito.when(itemRepository.findById(100)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(new ItemWriteDto("Item name",
                        "Desc",
                        true,
                        null,
                        10), 1, 100));
        assertEquals("Такого предмента не существует", ex.getMessage());
    }

    @Test
    void updateItem_whenUserNotOwner_thenThrowValidation() {
        Mockito.when(userRepository.existsById(1)).thenReturn(true);

        User otherUser = mock(User.class);
        Mockito.when(otherUser.getId()).thenReturn(2);

        Item item = new Item(otherUser, null);
        item.setName("Old");
        item.setDescription("Old desc");
        item.setAvailable(true);
        Mockito.when(itemRepository.findById(100)).thenReturn(Optional.of(item));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> itemService.updateItem(new ItemWriteDto("New",
                                "New desc",
                                false,
                                null,
                                null),
                        1,
                        100));
        assertEquals("Нарушение прав !", ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void updateItem_whenValid_thenUpdateAndReturnDto() {
        Mockito.when(userRepository.existsById(1)).thenReturn(true);

        Item item = new Item(user, null);
        item.setName("Old");
        item.setDescription("Old desc");
        item.setAvailable(true);
        ReflectionTestUtils.setField(item, "id", 100);

        Mockito.when(itemRepository.findById(100)).thenReturn(Optional.of(item));

        ItemWriteDto updateDto = new ItemWriteDto("New",
                "New desc",
                false,
                null,
                null);

        ItemReadDto updatedDto = itemService.updateItem(updateDto, 1, 100);

        assertEquals(100, updatedDto.getId());
        assertEquals("New", updatedDto.getName());
        assertEquals("New desc", updatedDto.getDescription());
        assertFalse(updatedDto.isAvailable());
    }

    @Test
    void getItemById_whenOwner_thenReturnItemWithBookings() {
        Item item = new Item(user, null);
        item.setName("Item1");
        item.setDescription("Desc1");
        item.setAvailable(true);
        ReflectionTestUtils.setField(item, "id", 1);

        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Mockito.when(commentRepository.findByItemId(100)).thenReturn(Collections.emptyList());

        Booking lastBooking = mock(Booking.class);
        Mockito.when(lastBooking.getId()).thenReturn(10);
        Mockito.when(lastBooking.getStart()).thenReturn(LocalDateTime.now().minusDays(2));
        Mockito.when(lastBooking.getEnd()).thenReturn(LocalDateTime.now().minusDays(1));
        Mockito.when(lastBooking.getItem()).thenReturn(item);
        Mockito.when(lastBooking.getBooker()).thenReturn(user);
        Mockito.when(lastBooking.getStatus()).thenReturn(Status.APPROVED);

        Booking nextBooking = mock(Booking.class);
        Mockito.when(nextBooking.getId()).thenReturn(11);
        Mockito.when(nextBooking.getStart()).thenReturn(LocalDateTime.now().plusDays(1));
        Mockito.when(nextBooking.getEnd()).thenReturn(LocalDateTime.now().plusDays(2));
        Mockito.when(nextBooking.getItem()).thenReturn(item);
        Mockito.when(nextBooking.getBooker()).thenReturn(user);
        Mockito.when(nextBooking.getStatus()).thenReturn(Status.WAITING);

        Mockito.when(bookingRepository.findTopByItemIdAndStartBefore(eq(1), any())).thenReturn(Optional.of(lastBooking));
        Mockito.when(bookingRepository.findTopByItemIdAndEndAfter(eq(1), any())).thenReturn(Optional.of(nextBooking));

        ItemReadDtoWithBookingsAndComments dto = itemService.getItemById(1, 1);

        assertEquals(1, dto.getId());
        assertEquals("Item1", dto.getName());
        assertEquals(true, dto.isAvailable());
        assertNotNull(dto.getLastBooking());
        assertEquals(10, dto.getLastBooking().getId());
        assertNotNull(dto.getNextBooking());
        assertEquals(11, dto.getNextBooking().getId());
    }

    @Test
    void getAllItemsByUser_whenUserNotFound_thenThrow() {
        Mockito.when(userRepository.existsById(1)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemService.getAllItemsByUser(1));
        assertEquals("Такого пользователя не существует", ex.getMessage());
    }

    @Test
    void getAllItemsByUser_whenFound_thenReturnList() {
        Mockito.when(userRepository.existsById(1)).thenReturn(true);

        Item item1 = new Item(user, null);
        item1.setName("Item1");
        item1.setDescription("Desc1");
        item1.setAvailable(true);

        Item item2 = new Item(user, null);
        item2.setName("Item2");
        item2.setDescription("Desc2");
        item2.setAvailable(false);

        Mockito.when(itemRepository.findByOwnerId(1)).thenReturn(List.of(item1, item2));

        List<ItemReadDto> dtos = itemService.getAllItemsByUser(1);

        assertEquals(2, dtos.size());
        assertEquals("Item1", dtos.get(0).getName());
        assertEquals("Item2", dtos.get(1).getName());
    }

    @Test
    void searchByText_whenEmpty_thenReturnEmpty() {
        List<ItemReadDto> result = itemService.searchByText("");
        assertTrue(result.isEmpty());
    }

    @Test
    void searchByText_whenFound_thenReturnList() {
        Item item = new Item(user, null);
        item.setDescription("Description");
        item.setName("Name");
        item.setAvailable(true);
        Mockito.when(itemRepository.searchByText("%TEXT%")).thenReturn(List.of(item));
        List<ItemReadDto> dtos = itemService.searchByText("text");
        assertFalse(dtos.isEmpty());
    }

    @Test
    void saveComment_whenUserOrItemNotFound_thenThrow() {
        CommentWriteDto commentWriteDto = new CommentWriteDto("text");

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException userEx = assertThrows(
                NotFoundException.class,
                () -> itemService.saveComment(commentWriteDto, 1, 100)
        );
        assertEquals("Такого пользователя не существует", userEx.getMessage());

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(100)).thenReturn(Optional.empty());

        NotFoundException itemEx = assertThrows(
                NotFoundException.class,
                () -> itemService.saveComment(commentWriteDto, 1, 100)
        );
        assertEquals("Такой вещи не существует", itemEx.getMessage());
    }

    @Test
    void saveComment_whenBookingNotExists_thenThrowValidationException() {
        int userId = 1;
        int itemId = 100;

        CommentWriteDto commentWriteDto = new CommentWriteDto("Nice item!");
        Item item = new Item(user, null);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.existsByBookerIdAndItemIdAndEndBefore(eq(userId), eq(itemId), any()))
                .thenReturn(false);

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> itemService.saveComment(commentWriteDto, userId, itemId)
        );

        assertEquals("Пользователь не брал в аренду эту вещь, либо бронирование ещё не завершилось !", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }
}
