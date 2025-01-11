package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@Data
public class ItemDtoResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<Comment> comments;
}
