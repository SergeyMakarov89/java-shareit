package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoResponse> getItemRequests() {
        log.info("Запустили метод получения всех запросов в контроллере");
        return itemRequestService.getItemRequests();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDtoResponse createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Запустили метод создания запроса в контроллере");
        return itemRequestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDtoResponse getItemRequestById(@PathVariable("requestId") Long requestId) {
        log.info("Запустили метод получения запроса по айди в контроллере");
        return itemRequestService.getItemRequestById(requestId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoResponse> getItemRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запустили метод получения всех запросов в контроллере");
        return itemRequestService.getItemRequestsByUserId(userId);
    }

}
