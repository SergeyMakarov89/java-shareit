package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.ItemDtoResponse;
import ru.practicum.shareit.user.User;

import java.util.List;

@Slf4j
public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(User requestor, ItemRequestDto request) {
        log.info("Запустили метод корвертации запроса вещи из айтемреквестдто в айтемреквест в айтемреквестмаппере");
        ItemRequest itemRequest = new ItemRequest();
        if (request.getDescription() != null) {
            itemRequest.setDescription(request.getDescription());
        }
        if (requestor != null) {
            itemRequest.setRequestor(requestor);
        }

        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        log.info("Запустили метод корвертации запроса вещи из айтемреквест в айтемреквестдто в айтемреквестмаппере");

        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription(itemRequest.getDescription());

        return dto;
    }

    public static ItemRequestDtoResponse mapToItemRequestDtoResponse(ItemRequest itemRequest) {
        log.info("Запустили метод корвертации запроса вещи из айтемреквест в айтемреквестдтореспонс в айтемреквестмаппере");

        ItemRequestDtoResponse dto = new ItemRequestDtoResponse();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestor(itemRequest.getRequestor());
        dto.setCreated(itemRequest.getCreated());

        return dto;
    }

    public static ItemRequestDtoResponse mapToItemRequestDtoResponseWithItems(ItemRequest itemRequest, List<ItemDtoResponse> items) {
        log.info("Запустили метод корвертации запроса вещи из айтемреквест в айтемреквестдтореспонс c айтемс в айтемреквестмаппере");

        ItemRequestDtoResponse dto = new ItemRequestDtoResponse();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestor(itemRequest.getRequestor());
        dto.setCreated(itemRequest.getCreated());
        dto.setItems(items);

        return dto;
    }

}
