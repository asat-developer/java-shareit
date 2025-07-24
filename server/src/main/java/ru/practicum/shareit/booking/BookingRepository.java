package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    //Для метода сервиса getBookingsByUser
    List<Booking> findByBookerId(Integer bookerId);

    @Query(value = "SELECT * FROM bookings b " +
            "WHERE b.start <= NOW " +
            "AND b.end >= NOW " +
            "AND b.booker_id = :userId ", nativeQuery = true)
    List<Booking> findByBookerIdAndByStartAndEndBetween(@Param("userId") Integer userId);

    List<Booking> findByBookerIdAndStartAfter(Integer userId, LocalDateTime now);

    List<Booking> findByBookerIdAndEndBefore(Integer userId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusIs(Integer userId, Status status);

    //Для метода сервиса getBookingsByOwner
    List<Booking> findByItemOwnerId(Integer userId);

    @Query(value = "SELECT * FROM bookings b " +
            "WHERE b.start <= NOW " +
            "AND b.end >= NOW " +
            "AND b.item.owner.id = :userId ", nativeQuery = true)
    List<Booking> findByOwnerIdAndByStartAndEndBetween(@Param("userId") Integer userId);

    List<Booking> findByItemOwnerIdAndStartAfter(Integer userId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndEndBefore(Integer userId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStatusIs(Integer userId, Status status);

    //Для проверки на отзыв
    Boolean existsByBookerIdAndItemIdAndEndBefore(Integer bookerId, Integer itemId, LocalDateTime now);

    //Для просмотра крайних бронирований
    Optional<Booking> findTopByItemIdAndStartBefore(Integer itemId, LocalDateTime now);

    Optional<Booking> findTopByItemIdAndEndAfter(Integer itemId, LocalDateTime now);
}
