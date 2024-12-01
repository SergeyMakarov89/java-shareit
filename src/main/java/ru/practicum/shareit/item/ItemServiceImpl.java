package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getItems() {
        log.info("Запустили метод получения всех вещей в сёрвисе");
        return itemRepository.getItems();
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        log.info("Запустили метод получения вещей по айди вещи в сёрвисе");
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        log.info("Запустили метод получения вещей по айди пользователя в сёрвисе");
        return itemRepository.getItemsByUserId(userId);
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        log.info("Запустили метод создания вещи в сёрвисе");
        if (userRepository.getUserByIdNotDto(userId) == null) {
            throw new NotFoundException("Пользователь с таким айди не найден");
        }
        User owner = userRepository.getUserByIdNotDto(userId);
        itemDto.setOwner(owner);
        return itemRepository.createItem(itemDto);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        log.info("Запустили метод обновления вещи в сёрвисе");
        if (!itemRepository.getItemById(itemId).getOwner().getId().equals(userId)) {
            throw new NotFoundException("У этой вещи другой владелец");
        }
        User owner = userRepository.getUserByIdNotDto(userId);
        itemDto.setOwner(owner);
        return itemRepository.updateItem(itemId, itemDto);
    }

    @Override
    public void deleteItem(Long itemId) {
        log.info("Запустили метод удаения вещи в сёрвисе");
        itemRepository.deleteItem(itemId);
    }

    @Override
    public List<ItemDto> findItems(String text) {
        log.info("Запустили метод поиска вещи в сёрвисе");
        return itemRepository.findItems(text);
    }
}
