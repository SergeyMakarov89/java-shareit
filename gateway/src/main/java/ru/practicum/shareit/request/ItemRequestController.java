package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequests() {
        log.info("Запустили метод getItemRequests в контроллере Gateway");
        return itemRequestClient.getItemRequests();
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequestDto request) {
        log.info("Запустили метод createItemRequest в контроллере Gateway");
        return itemRequestClient.createItemRequest(userId, request);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable("requestId") Long requestId) {
        log.info("Запустили метод getItemRequestById в контроллере Gateway");
        return itemRequestClient.getItemRequestById(requestId);
    }

    @GetMapping()
    public ResponseEntity<Object> getItemRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запустили метод getItemRequestsByUserId в контроллере Gateway");
        return itemRequestClient.getItemRequestsByUserId(userId);
    }
}
