package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void saveItem_shouldPersistItemInDatabase() {
        Item item = new Item();
        item.setName("Table");
        item.setDescription("Wooden");
        item.setAvailable(true);

        Item savedItem = itemRepository.save(item);
        assertThat(savedItem.getId()).isNotNull();
        Optional<Item> foundItem = itemRepository.findById(savedItem.getId());
        assertThat(foundItem).isPresent();
        assertThat(foundItem.get().getName()).isEqualTo("Table");
        assertThat(foundItem.get().getDescription()).isEqualTo("Wooden");
        assertThat(foundItem.get().getAvailable()).isEqualTo(true);
    }
}
