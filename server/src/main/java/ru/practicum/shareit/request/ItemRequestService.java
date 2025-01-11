package ru.practicum.shareit.request;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDtoResponse> getItemRequests();

    ItemRequestDtoResponse createItemRequest(Long userId, ItemRequestDto itemRequestDto);

    ItemRequestDtoResponse getItemRequestById(Long itemRequestId);

    List<ItemRequestDtoResponse> getItemRequestsByUserId(Long userId);
}
