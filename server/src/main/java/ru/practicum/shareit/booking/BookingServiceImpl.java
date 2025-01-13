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
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование с таким айди не найдено"));
        return BookingMapper.mapToBookingDtoResponse(booking);
    }

    @Override
    public List<BookingDto> getBookingsByItemId(Long itemId) {
        log.info("Запустили метод получения бронирований по айди вещи в сёрвисе");
        return bookingListToBookingDtoList(bookingRepository.findByItemId(itemId));
    }

    @Override
    @Transactional
    public BookingDtoResponse createBooking(Long userId, BookingDto request) {
        log.info("Запустили метод создания бронирования в сёрвисе");
        if (itemRepository.findById(request.getItemId()).isEmpty()) {
            throw new NotFoundException("Вещь с таким айди не найдена");
        }
        if (!userRepository.existsById(userId)) {
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
        if (itemRepository.findById(request.getItemId()).orElseThrow().getAvailable().equals(false)) {
            throw new ValidationException("Вещь с таким айди сейчас занята");
        }
        request.setStatus(BookingStatus.WAITING);
        request.setBooker(userRepository.findById(userId).orElseThrow());

        return BookingMapper.mapToBookingDtoResponse(bookingRepository.save(BookingMapper.mapToBooking(request, itemRepository.findById(request.getItemId()).orElseThrow())));
    }

    @Override
    @Transactional
    public BookingDtoResponse updateBooking(Long userId, Long bookingId, Boolean approved) {
        log.info("Запустили метод обновления бронирования в сёрвисе");
        if (!userRepository.existsById(userId)) {
            throw new ValidationException("Владелец вещи не найден");
        }
        if (!userRepository.findById(userId).orElseThrow().getId().equals(bookingRepository.findById(bookingId).orElseThrow().getItem().getOwner().getId())) {
            throw new NotFoundException("У этой вещи другой владелец");
        }

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование с таким айди не найдено"));;
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
        return bookingListToBookingDtoList(bookingRepository.findByBookerIdOrderByStartDesc(bookerId));
    }


    @Override
    public List<BookingDtoResponse> getBookingsByUserIdAndStatus(Long userId, SearchType searchType) {

        switch (searchType) {
            case ALL -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findByBookerIdOrderByStartDesc(userId));
            }
            case PAST -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now()));
            }
            case CURRENT -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findCurrentBookingsByUser(userId, LocalDateTime.now(), LocalDateTime.now()));
            }
            case FUTURE -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now()));
            }
            case WAITING -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING));
            }
            case REJECTED -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED));
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    @Override
    public List<BookingDtoResponse> getBookingsByUserItemsAndStatus(Long userId, SearchType searchType) {

        if (!itemRepository.existsByOwnerId(userId)) {
            throw new NotFoundException("У этого пользователя нет вещей для бронирования");
        }

        switch (searchType) {
            case ALL -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findByItemOwnerIdOrderByStartDesc(userId));
            }
            case PAST -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now()));
            }
            case CURRENT -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findCurrentBookingsByUserItems(userId, LocalDateTime.now(), LocalDateTime.now()));
            }
            case FUTURE -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now()));
            }
            case WAITING -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findByItemIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING));
            }
            case REJECTED -> {
                return bookingListToBookingDtoResponseList(bookingRepository.findByItemIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED));
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    public List<BookingDtoResponse> bookingListToBookingDtoResponseList(List<Booking> bookingList) {

        List<BookingDtoResponse> bookingDtoResponseList = new ArrayList<>();

        for (Booking booking : bookingList) {
            bookingDtoResponseList.add(BookingMapper.mapToBookingDtoResponse(booking));
        }

        return bookingDtoResponseList;
    }

    public List<BookingDto> bookingListToBookingDtoList(List<Booking> bookingList) {

        List<BookingDto> bookingDtoList = new ArrayList<>();

        for (Booking booking : bookingList) {
            bookingDtoList.add(BookingMapper.mapToBookingDto(booking));
        }

        return bookingDtoList;
    }
}
