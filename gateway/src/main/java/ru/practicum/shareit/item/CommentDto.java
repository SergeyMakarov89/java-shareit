package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private ItemDto item;
    private UserDto author;
    private LocalDateTime created;
}
