package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.enums.SearchType;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDtoResponse getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("bookingId") Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDtoResponse createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDtoResponse updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("bookingId") Long bookingId, @RequestParam Boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDtoResponse> getBookingsByUserIdAndStatus(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL")SearchType searchType) {
        return bookingService.getBookingsByUserIdAndStatus(userId, searchType);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDtoResponse> getBookingsByUserItemsAndStatus(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL")SearchType searchType) {
        return bookingService.getBookingsByUserItemsAndStatus(userId, searchType);
    }
}
