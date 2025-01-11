package ru.practicum.shareit.item.comment;

import lombok.Data;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;

@Data
public class CommentDtoResponse {
    private Long id;
    private String text;
    private Item item;
    private String authorName;
    private LocalDateTime created;
}
