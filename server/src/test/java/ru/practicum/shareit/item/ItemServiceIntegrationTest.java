package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.item.dto.ItemReadDto;
import ru.practicum.shareit.item.dto.ItemReadDtoWithBookingsAndComments;
import ru.practicum.shareit.item.dto.ItemWriteDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceIntegrationTest {

    private final ItemService itemService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Test
    void saveItem_shouldPersistItem() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");

        User savedUser = userRepository.save(user);

        ItemWriteDto dto = new ItemWriteDto("Table", "Wooden", true, 1, null);

        ItemReadDto savedItem = itemService.saveItem(dto, 1);

        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getName()).isEqualTo(dto.getName());
        assertThat(savedItem.getDescription()).isEqualTo(dto.getDescription());
        assertThat(savedItem.getOwnerId()).isEqualTo(dto.getOwnerId());
    }

    @Test
    void saveComment_shouldPersistComment() {
        User user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@example.com");

        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        Item item = new Item(savedUser1, null);
        item.setAvailable(true);
        item.setDescription("Wooden");
        item.setName("Table");

        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking(savedItem, savedUser2);
        LocalDateTime fixedNow = LocalDateTime.of(2025, 7, 24, 12, 0);
        booking.setStart(fixedNow.minusDays(2));
        booking.setEnd(fixedNow.minusDays(1));
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        Comment comment = new Comment(item, savedUser2, LocalDateTime.now());
        comment.setText("Best Thing !");

        commentRepository.save(comment);

        ItemReadDtoWithBookingsAndComments result = itemService.getItemById(savedItem.getId(), savedUser1.getId());
        assertNotNull(result);
        assertEquals(1, result.getComments().size());
    }
}

