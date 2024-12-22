package ru.practicum.shareit.booking;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private Long itemId;
    private User booker;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
