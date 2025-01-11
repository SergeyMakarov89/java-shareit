package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
public class ItemDto {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private UserDto owner;
    private Long requestId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
