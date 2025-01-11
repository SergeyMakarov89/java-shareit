package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByItemId(Long itemId);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime dateTime);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime dateTime);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start < :dateTime1 AND b.end > :dateTime2 ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByUser(Long bookerId, LocalDateTime dateTime1, LocalDateTime dateTime2);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start < :dateTime1 AND b.end > :dateTime2 ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByUserItems(Long ownerId, LocalDateTime dateTime1, LocalDateTime dateTime2);

    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long ownerId, LocalDateTime dateTime);

    List<Booking> findByItemOwnerIdAndEndIsBefore(Long ownerId, LocalDateTime dateTime);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    List<Booking> findByBookerIdAndItemIdAndEndIsBefore(Long bookerId, Long itemId, LocalDateTime dateTime);

    List<Booking> findByItemIdAndStatusOrderByStartDesc(Long itemId, BookingStatus status);
}
