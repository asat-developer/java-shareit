package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByOwnerId(Integer userId);

    @Query(value = "SELECT * " +
            "FROM items i " +
            "WHERE i.available = true " +
            "AND (UPPER(i.name) LIKE :text) OR (UPPER(i.description) LIKE :text)",
             nativeQuery = true)
    List<Item> searchByText(@Param("text") String text);
}
