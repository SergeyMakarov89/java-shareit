package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {
    List<ItemDto> getItems();

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemsByUserId(Long userId);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    void deleteItem(Long itemId);

    List<ItemDto> findItems(String text);
}
