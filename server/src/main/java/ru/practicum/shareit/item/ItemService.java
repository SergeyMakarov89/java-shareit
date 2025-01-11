package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentDtoResponse;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItems();

    ItemDtoResponse getItemById(Long itemId);

    List<ItemDto> getItemsByUserId(Long userId);

    ItemDto createItem(Long userId, ItemDto request);

    ItemDto updateItem(Long userId, Long itemId, ItemDto request);

    void deleteItem(Long itemId);

    List<ItemDto> findItems(String text);

    CommentDtoResponse createComment(Long itemId, Long userId, CommentDto commentDto);
}
