package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;

@Slf4j
@Component
public class BookingMapper {

    public static Booking mapToBooking(BookingDto request, Item item) {
        log.info("Запустили метод корвертации бронирования из букингдто в букинг в букингмаппере");
        Booking booking = new Booking();
        if (request.getId() != null) {
            booking.setId(request.getId());
        }
        if (request.getStart() != null) {
            booking.setStart(request.getStart());
        }
        if (request.getEnd() != null) {
            booking.setEnd(request.getEnd());
        }
        if (request.getItemId() != null) {
            booking.setItem(item);
        }
        if (request.getBooker() != null) {
            booking.setBooker(request.getBooker());
        }
        if (request.getStatus() != null) {
            booking.setStatus(request.getStatus());
        }

        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        log.info("Запустили метод корвертации бронирования из букинг в букингдто в букингмаппере");

        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setItemId(booking.getItem().getId());
        dto.setBooker(booking.getBooker());
        dto.setStatus(booking.getStatus());

        return dto;
    }

    public static BookingDtoResponse mapToBookingDtoResponse(Booking booking) {
        log.info("Запустили метод корвертации бронирования из букинг в букингдтореспонс в букингмаппере");

        BookingDtoResponse dtoResponse = new BookingDtoResponse();
        dtoResponse.setId(booking.getId());
        dtoResponse.setStart(booking.getStart());
        dtoResponse.setEnd(booking.getEnd());
        dtoResponse.setItem(booking.getItem());
        dtoResponse.setBooker(booking.getBooker());
        dtoResponse.setStatus(booking.getStatus());

        return dtoResponse;
    }
}
