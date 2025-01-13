package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.CommentDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable("itemId") Long itemId) {
        log.info("Запустили метод getItemById в контроллере Gateway");
        return itemClient.getItemById(itemId);
    }

    @GetMapping()
    public ResponseEntity<Object> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запустили метод getItemsByUserId в контроллере Gateway");
        return itemClient.getItemsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto request) {
        log.info("Запустили метод createItem в контроллере Gateway");
        return itemClient.createItem(userId, request);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId, @RequestBody ItemDto request) {
        log.info("Запустили метод updateItem в контроллере Gateway");
        return itemClient.updateItem(userId, itemId, request);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable("itemId") Long itemId) {
        log.info("Запустили метод deleteItem в контроллере Gateway");
        return itemClient.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId, @Valid @RequestBody CommentDto commentDto) {
        log.info("Запустили метод createComment в контроллере Gateway");
        return itemClient.createComment(userId, itemId, commentDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItems(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        log.info("Запустили метод findItems в контроллере Gateway");
        return itemClient.findItems(userId, text);
    }
}
