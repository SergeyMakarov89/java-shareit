package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ItemMapper {

    public Item mapToItem(ItemDto request) {
        log.info("Запустили метод корвертации вещи из айтемдто в айтем в айтеммаппере");
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        item.setOwner(request.getOwner());
        item.setRequest(request.getRequest());

        return item;
    }

    public ItemDto mapToItemDto(Item item) {
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
}
