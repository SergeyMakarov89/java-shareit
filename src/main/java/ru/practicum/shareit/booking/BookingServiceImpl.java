package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.enums.SearchType;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<BookingDto> getBookings() {
        log.info("Запустили метод получения всех бронирований в сёрвисе");
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookingRepository.findAll()) {
            bookingDtoList.add(BookingMapper.mapToBookingDto(booking));
        }
        return bookingDtoList;
    }

    @Override
    public BookingDtoResponse getBookingById(Long userId, Long bookingId) {
        log.info("Запустили метод получения бронирований по айди бронирования в сёрвисе");
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким айди не найден");
        }
        Booking booking = bookingRepository.findById(bookingId).get();
        return BookingMapper.mapToBookingDtoResponse(booking);
    }

    @Override
    public List<BookingDto> getBookingsByItemId(Long itemId) {
        log.info("Запустили метод получения бронирований по айди вещи в сёрвисе");
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookingRepository.findByItemId(itemId)) {
            bookingDtoList.add(BookingMapper.mapToBookingDto(booking));
        }
        return bookingDtoList;
    }

    @Override
    @Transactional
    public BookingDtoResponse createBooking(Long userId, BookingDto request) {
        log.info("Запустили метод создания бронирования в сёрвисе");
        if (itemRepository.findById(request.getItemId()).isEmpty()) {
            throw new NotFoundException("Вещь с таким айди не найдена");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким айди не найден");
        }
        if (request.getEnd().isBefore(request.getStart())) {
            throw new ValidationException("Дата окончания бронирования не может быть до даты начала бронирования");
        }
        if (request.getStart().isAfter(request.getEnd())) {
            throw new ValidationException("Дата окончания начала бронирования не может быть после даты окончания бронирования");
        }
        if (request.getStart().equals(request.getEnd())) {
            throw new ValidationException("Дата начала бронирования не может быть одинаковой с датой окончания бронирования");
        }
        if (itemRepository.findById(request.getItemId()).get().getAvailable().equals(false)) {
            throw new ValidationException("Вещь с таким айди сейчас занята");
        }
        request.setStatus(BookingStatus.WAITING);
        request.setBooker(userRepository.findById(userId).get());

        return BookingMapper.mapToBookingDtoResponse(bookingRepository.save(BookingMapper.mapToBooking(request, itemRepository.findById(request.getItemId()).get())));
    }

    @Override
    @Transactional
    public BookingDtoResponse updateBooking(Long userId, Long bookingId, Boolean approved) {
        log.info("Запустили метод обновления бронирования в сёрвисе");
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new NotFoundException("Бронирование с таким айди не найдено");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new ValidationException("Владелец вещи не найден");
        }
        if (!userRepository.findById(userId).get().getId().equals(bookingRepository.findById(bookingId).get().getItem().getOwner().getId())) {
            throw new NotFoundException("У этой вещи другой владелец");
        }

        Booking booking = bookingRepository.findById(bookingId).get();
        if (approved == true) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.mapToBookingDtoResponse(booking);
    }

    @Override
    public List<BookingDto> getByBookerById(Long bookerId) {
        log.info("Запустили метод получения бронирований по айди пользователя в сёрвисе");
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookingRepository.findByBookerIdOrderByStartDesc(bookerId)) {
            bookingDtoList.add(BookingMapper.mapToBookingDto(booking));
        }
        return bookingDtoList;
    }


    @Override
    public List<BookingDtoResponse> getBookingsByUserIdAndStatus(Long userId, SearchType searchType) {

        List<BookingDtoResponse> bookingDtoResponseList = new ArrayList<>();

        switch (searchType) {
            case ALL -> {
                for (Booking booking : bookingRepository.findByBookerIdOrderByStartDesc(userId)) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            case PAST -> {
                for (Booking booking : bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now())) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            case CURRENT -> {
                for (Booking booking : bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now())) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            case FUTURE -> {
                for (Booking booking : bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now())) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            case WAITING -> {
                for (Booking booking : bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING)) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            case REJECTED -> {
                for (Booking booking : bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED)) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    @Override
    public List<BookingDtoResponse> getBookingsByUserItemsAndStatus(Long userId, SearchType searchType) {

        List<BookingDtoResponse> bookingDtoResponseList = new ArrayList<>();

        if (itemRepository.findByOwnerId(userId).isEmpty()) {
            throw new NotFoundException("У этого пользователя нет вещей для бронирования");
        }

        switch (searchType) {
            case ALL -> {
                for (Booking booking : bookingRepository.findByItemOwnerIdOrderByStartDesc(userId)) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            case PAST -> {
                for (Booking booking : bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now())) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            case CURRENT -> {
                for (Booking booking : bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now())) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            case FUTURE -> {
                for (Booking booking : bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now())) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            case WAITING -> {
                for (Booking booking : bookingRepository.findByItemIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING)) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            case REJECTED -> {
                for (Booking booking : bookingRepository.findByItemIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED)) {
                    bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
                }
                return bookingDtoResponseList;
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }
}
