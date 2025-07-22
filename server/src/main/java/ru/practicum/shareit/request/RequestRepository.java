package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.dto.RequestWithItems;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Integer> {

    @Query(value = "SELECT r.request_id AS requestId, " +
            "r.description AS description, " +
            "r.time_created AS timeCreated, " +
            "r.requestor_id AS requestorId, " +
            "i.item_id AS itemId, " +
            "i.name AS name, " +
            "i.owner_id AS ownerId " +
            "FROM requests r " +
            "LEFT JOIN items i ON r.request_id = i.request_id " +
            "WHERE r.requestor_id = :userId", nativeQuery = true)
    List<RequestWithItems> findByRequestorIdWithItems(Integer userId);

    List<ItemRequest> findAllByOrderByTimeCreatedDesc();

    @Query(value = "SELECT r.request_id AS requestId, " +
            "r.description AS description, " +
            "r.time_created AS timeCreated, " +
            "r.requestor_id AS requestorId, " +
            "i.item_id AS itemId, " +
            "i.name AS name, " +
            "i.owner_id AS ownerId " +
            "FROM requests r " +
            "LEFT JOIN items i ON r.request_id = i.request_id " +
            "WHERE r.request_id = :requestId", nativeQuery = true)
    RequestWithItems findByIdWithItems(Integer requestId);
}
