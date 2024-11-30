package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private Map<Long, Item> items = new HashMap<>();
    private long idCount = 0;
    private final ItemMapper itemMapper;

    public List<ItemDto> getItems() {
        log.info("Запустили метод получения всех вещей в репозитории");
        List<Item> itemsList = items.values().stream().toList();
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemsList) {
            itemDtoList.add(itemMapper.mapToItemDto(item));
        }
        log.info("Собрали все вещи в список в репозитории");
        return itemDtoList;
    }

    public ItemDto getItemById(Long itemId) {
        log.info("Запустили метод получения вещи по айди в репозитории");
        return itemMapper.mapToItemDto(items.get(itemId));
    }

    public List<ItemDto> getItemsByUserId(Long userId) {
        log.info("Запустили метод получения вещей по айди пользователя в репозитории");
        List<Item> itemsList = items.values().stream().toList();
        List<Item> itemsListByUser = new ArrayList<>();
        for (Item item : itemsList) {
            if (item.getOwner().getId().equals(userId)) {
                itemsListByUser.add(item);
            }
        }
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemsListByUser) {
            itemDtoList.add(itemMapper.mapToItemDto(item));
        }
        log.info("Собрали вещи по айди пользователя в список в репозитории");
        return itemDtoList;
    }

    public ItemDto createItem(ItemDto itemDto) {
        log.info("Запустили метод создания вещи в репозитории");
        Item item = itemMapper.mapToItem(itemDto);
        idCount = idCount + 1;
        item.setId(idCount);
        items.put(idCount, item);
        log.info("Сохранили новую вещь в репозитории");
        return itemMapper.mapToItemDto(item);
    }

    public ItemDto updateItem(Long itemId, ItemDto itemDto) {
        log.info("Запустили метод обновления вещи в репозитории");
        Item newItem = itemMapper.mapToItem(itemDto);

        Item oldItem = items.get(itemId);
        if (itemDto.getName() != null) {
            oldItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            oldItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            oldItem.setAvailable(newItem.getAvailable());
        }
        if (newItem.getOwner() != null) {
            oldItem.setOwner(newItem.getOwner());
        }
        if (newItem.getRequest() != null) {
            oldItem.setRequest(newItem.getRequest());
        }
        items.put(itemId, oldItem);
        log.info("Обновили вещь в репозитории");
        return itemMapper.mapToItemDto(oldItem);
    }

    public void deleteItem(Long itemId) {
        log.info("Запустили метод удаления вещи в репозитории");
        items.remove(itemId);
        log.info("Удалили вещь в репозитории");
    }

    public List<ItemDto> findItems(String text) {
        log.info("Запустили метод поиска вещей в репозитории");
        List<Item> itemsList = new ArrayList<>();
        List<ItemDto> itemsListDto = new ArrayList<>();
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        for (Item item : items.values()) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                if (item.getAvailable().equals(true)) {
                    itemsList.add(item);
                }
            }
        }
        for (Item item : itemsList) {
            itemsListDto.add(itemMapper.mapToItemDto(item));
        }
        log.info("Нашли вещи по запросу в репозитории");
        return itemsListDto;
    }
}
