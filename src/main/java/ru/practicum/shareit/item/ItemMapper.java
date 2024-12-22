package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.comment.Comment;

import java.util.List;

@Slf4j
public class ItemMapper {

    public static Item mapToItem(ItemDto request) {
        log.info("Запустили метод корвертации вещи из айтемдто в айтем в айтеммаппере");
        Item item = new Item();
        if (request.getId() != null) {
            item.setId(request.getId());
        }
        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }
        if (request.getAvailable() != null) {
            item.setAvailable(request.getAvailable());
        }
        if (request.getOwner() != null) {
            item.setOwner(request.getOwner());
        }
        if (request.getRequest() != null) {
            item.setRequest(request.getRequest());
        }

        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        log.info("Запустили метод корвертации вещи из айтем в айтемдто в айтеммаппере");

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwner(item.getOwner());
        dto.setRequest(item.getRequest());

        return dto;
    }

    public static ItemDtoResponse mapToItemDtoResponse(Item item, List<Comment> comments) {
        log.info("Запустили метод корвертации вещи из айтем в айтемдтореспонс в айтеммаппере");

        ItemDtoResponse dto = new ItemDtoResponse();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwner(item.getOwner());
        dto.setRequest(item.getRequest());
        dto.setComments(comments);

        return dto;
    }
}
