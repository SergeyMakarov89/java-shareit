package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDtoResponse;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<ItemRequestDtoResponse> getItemRequests() {
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByOrderByCreatedDesc();
        return itemRequestListToItemRequestDtoResponseList(itemRequestList);
    }

    @Override
    @Transactional
    public ItemRequestDtoResponse createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        if (!userRepository.existsById(userId)) {
            throw new ValidationException("Пользователь не найден");
        }

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(userRepository.findById(userId).orElseThrow(), itemRequestDto);

        return ItemRequestMapper.mapToItemRequestDtoResponse(itemRequestRepository.save(itemRequest));
    }


    @Override
    public ItemRequestDtoResponse getItemRequestById(Long itemRequestId) {
        if (!itemRequestRepository.existsById(itemRequestId)) {
            throw new ValidationException("Запрос не найден");
        }

        List<ItemDtoResponse> itemDtoResponseList = new ArrayList<>();

        for (Item item : itemRepository.findByRequestId(itemRequestId)) {
            itemDtoResponseList.add(ItemMapper.mapToItemDtoResponse(item));
        }

        return ItemRequestMapper.mapToItemRequestDtoResponseWithItems(itemRequestRepository.findById(itemRequestId).orElseThrow(), itemDtoResponseList);
    }

    @Override
    public List<ItemRequestDtoResponse> getItemRequestsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ValidationException("Пользователь не найден");
        }

        List<ItemRequest> itemRequestList = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        List<Long> itemRequestIdsList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequestList) {
            itemRequestIdsList.add(itemRequest.getId());
        }

        List<Item> itemsList = itemRepository.findByRequestIdIn(itemRequestIdsList);
        List<ItemDtoResponse> itemDtoResponseList = new ArrayList<>();

        for (Item item : itemsList) {
            itemDtoResponseList.add(ItemMapper.mapToItemDtoResponse(item));
        }

        Map<Long, List<ItemDtoResponse>> itemsByRequestId = new HashMap<>();

        for (ItemDtoResponse item : itemDtoResponseList) {
            Long requestId = item.getRequest().getId();
            if (!itemsByRequestId.containsKey(requestId)) {
                itemsByRequestId.put(requestId, new ArrayList<>());
            }
            itemsByRequestId.get(requestId).add(item);
        }

        List<ItemRequestDtoResponse> itemRequestDtoResponseList = new ArrayList<>();

        for (ItemRequest itemRequest : itemRequestList) {
            List<ItemDtoResponse> items = itemsByRequestId.get(itemRequest.getId());
            ItemRequestDtoResponse itemRequestDtoResponse = ItemRequestMapper.mapToItemRequestDtoResponseWithItems(itemRequest, items);
            itemRequestDtoResponseList.add(itemRequestDtoResponse);
        }

        return itemRequestDtoResponseList;
    }

    public List<ItemRequestDtoResponse> itemRequestListToItemRequestDtoResponseList(List<ItemRequest> itemRequestList) {

        List<ItemRequestDtoResponse> itemRequestDtoResponseList = new ArrayList<>();

        for (ItemRequest itemRequest : itemRequestList) {
            itemRequestDtoResponseList.add(ItemRequestMapper.mapToItemRequestDtoResponse(itemRequest));
        }

        return itemRequestDtoResponseList;
    }
}
