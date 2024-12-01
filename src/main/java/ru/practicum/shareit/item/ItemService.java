package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItems();

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemsByUserId(Long userId);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    void deleteItem(Long itemId);

    List<ItemDto> findItems(String text);
}
