package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long userId);

    Boolean existsByOwnerId(Long userId);

    @Query(value = "SELECT item FROM Item as item WHERE item.available = TRUE AND LOWER(item.name) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(item.description) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Item> findItems(String text);
}
