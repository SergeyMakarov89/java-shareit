package ru.practicum.shareit.booking;

import ru.practicum.shareit.enums.SearchType;

import java.util.List;

public interface BookingService {
    List<BookingDto> getBookings();

    BookingDtoResponse getBookingById(Long userId, Long bookingId);

    List<BookingDto> getByBookerById(Long bookerId);

    List<BookingDto> getBookingsByItemId(Long itemId);

    BookingDtoResponse createBooking(Long userId, BookingDto bookingDto);

    BookingDtoResponse updateBooking(Long userId, Long bookingId, Boolean approved);

    List<BookingDtoResponse> getBookingsByUserIdAndStatus(Long userId, SearchType searchStatus);

    List<BookingDtoResponse> getBookingsByUserItemsAndStatus(Long userId, SearchType searchStatus);
}
