package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.item.ItemDtoResponse;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDtoResponse {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
    private List<ItemDtoResponse> items;
}
