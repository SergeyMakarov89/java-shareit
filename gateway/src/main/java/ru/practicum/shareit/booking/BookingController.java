package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.SearchType;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("bookingId") Long bookingId) {
		log.info("Запустили метод getBookingById в контроллере Gateway");
		return bookingClient.getBookingById(userId, bookingId);
	}

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingDto request) {
		log.info("Запустили метод createBooking в контроллере Gateway");
		return bookingClient.createBooking(userId, request);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("bookingId") Long bookingId, @RequestParam Boolean approved) {
		log.info("Запустили метод updateBooking в контроллере Gateway");
		return bookingClient.updateBooking(userId, bookingId, approved);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingsByUserIdAndStatus(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") SearchType searchType) {
		log.info("Запустили метод getBookingsByUserIdAndStatus в контроллере Gateway");
		return bookingClient.getBookingsByUserIdAndStatus(userId, searchType);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsByUserItemsAndStatus(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") SearchType searchType) {
		log.info("Запустили метод getBookingsByUserItemsAndStatus в контроллере Gateway");
		return bookingClient.getBookingsByUserItemsAndStatus(userId, searchType);
	}
}
