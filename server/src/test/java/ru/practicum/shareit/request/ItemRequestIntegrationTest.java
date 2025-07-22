package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestReadDtoWithItems;
import ru.practicum.shareit.request.dto.ItemRequestWriteDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestIntegrationTest {

    private final RequestService requestService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;

    @Test
    void getItemRequestsByRequestorIdWithItems() {
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john31@example.com");
        User savedUser1 = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Bob Brown");
        user2.setEmail("brown@example.com");
        User savedUser2 = userRepository.save(user2);

        ItemRequestWriteDto requestWriteDto = new ItemRequestWriteDto("Need Drill");
        requestService.saveRequest(requestWriteDto, savedUser1.getId());
        ItemRequest request = requestRepository.findById(1).get();

        Item item = new Item(user2, request);
        item.setName("Drill");
        item.setDescription("Diamond Drill");
        item.setAvailable(true);

        itemRepository.save(item);

        ItemRequestReadDtoWithItems savedRequest = requestService.getRequestById(1);
        assertThat(savedRequest.getId()).isNotNull();
        assertThat(savedRequest.getItems().getFirst().getName()).isEqualTo("Drill");
        assertThat(savedRequest.getRequestorId()).isEqualTo(savedUser1.getId());
    }
}


