package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    public List<ItemDto> getItems() {
        log.info("Запустили метод получения всех вещей в сёрвисе");
        return itemRepository.findAll().stream()
                .map(ItemMapper::mapToItemDto)
                .collect(toList());
    }

    @Override
    public ItemDtoResponse getItemById(Long itemId) {
        log.info("Запустили метод получения вещей по айди вещи в сёрвисе");
        Item item = itemRepository.findById(itemId).orElseThrow();
        List<Comment> comments = commentRepository.findByItemId(itemId);
        return ItemMapper.mapToItemDtoResponse(item, comments);
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        log.info("Запустили метод получения вещей по айди пользователя в сёрвисе");
        return itemRepository.findByOwnerId(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public ItemDto createItem(Long userId, ItemDto request) {
        log.info("Запустили метод создания вещи в сёрвисе");
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ValidationException("Навазние вещи должно быть указано");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким айди не найден");
        }
        request.setOwner(userRepository.findById(userId).orElseThrow());
        return ItemMapper.mapToItemDto(itemRepository.save(ItemMapper.mapToItem(request)));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto request) {
        log.info("Запустили метод обновления вещи в сёрвисе");

        request.setId(itemId);
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Владелец вещи не найден");
        }
        request.setOwner(userRepository.findById(userId).orElseThrow());
        if (request.getAvailable() == null) {
            request.setAvailable(itemRepository.findById(itemId).orElseThrow().getAvailable());
        }
        if (request.getName() == null) {
            request.setName(itemRepository.findById(itemId).orElseThrow().getName());
        }
        if (request.getDescription() == null) {
            request.setDescription(itemRepository.findById(itemId).orElseThrow().getDescription());
        }

        Item item = itemRepository.save(ItemMapper.mapToItem(request));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long itemId) {
        log.info("Запустили метод удаения вещи в сёрвисе");
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> findItems(String text) {
        log.info("Запустили метод поиска вещи в сёрвисе");
        if (text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<ItemDto> itemDtoList = new ArrayList<>();
            for (Item item : itemRepository.findItems(text)) {
                itemDtoList.add(ItemMapper.mapToItemDto(item));
            }
            return itemDtoList;
        }
    }

    @Override
    @Transactional
    public CommentDtoResponse createComment(Long itemId, Long userId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        if (bookingRepository.findByBookerIdAndItemIdAndEndIsBefore(userId, itemId, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("Этот пользователь не брал в аренду эту вещь");
        }

        Comment comment = CommentMapper.mapToComment(commentDto);

        comment.setItem(item);
        comment.setAuthor(user);

        return CommentMapper.mapToCommentDtoResponse(commentRepository.save(comment));
    }
}
