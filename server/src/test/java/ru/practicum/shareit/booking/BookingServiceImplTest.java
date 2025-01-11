package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.enums.SearchType;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentDtoResponse;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class BookingServiceImplTest {
    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void start() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void testGetBookings() {
        User user1 = new User();
        user1.setName("User9");
        user1.setEmail("user9@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item10");
        itemDto.setDescription("Description10");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User10");
        user2.setEmail("user10@user.com");
        Long userId2 = userRepository.save(user2).getId();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription2");

        itemRequestRepository.save(itemRequest);

        Booking booking = new Booking();
        booking.setItem(itemRepository.findById(itemId).orElseThrow());
        booking.setBooker(userRepository.findById(userId).orElseThrow());
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusHours(5));
        booking.setEnd(LocalDateTime.now());
        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText2");

        CommentDtoResponse commentDtoResponse = itemService.createComment(itemId, userId, commentDto);

        assertEquals(1, bookingService.getBookings().size());
    }

    @Test
    void testGetBookingById() {
        User user1 = new User();
        user1.setName("User11");
        user1.setEmail("user11@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item11");
        itemDto.setDescription("Description11");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User12");
        user2.setEmail("user12@user.com");
        Long userId2 = userRepository.save(user2).getId();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription3");

        itemRequestRepository.save(itemRequest);

        Booking booking = new Booking();
        booking.setItem(itemRepository.findById(itemId).orElseThrow());
        booking.setBooker(userRepository.findById(userId).orElseThrow());
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusHours(5));
        booking.setEnd(LocalDateTime.now());
        Long bookingId = bookingRepository.save(booking).getId();

        assertEquals(bookingId, bookingService.getBookingById(userId, bookingId).getId());
        assertEquals(itemId, bookingService.getBookingById(userId, bookingId).getItem().getId());
        assertEquals(userId, bookingService.getBookingById(userId, bookingId).getBooker().getId());
    }

    @Test
    void testGetBookingsByItemId() {
        User user1 = new User();
        user1.setName("User12");
        user1.setEmail("user12@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item12");
        itemDto.setDescription("Description12");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User13");
        user2.setEmail("user13@user.com");
        Long userId2 = userRepository.save(user2).getId();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription4");

        itemRequestRepository.save(itemRequest);

        Booking booking = new Booking();
        booking.setItem(itemRepository.findById(itemId).orElseThrow());
        booking.setBooker(userRepository.findById(userId).orElseThrow());
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusHours(5));
        booking.setEnd(LocalDateTime.now());
        Long bookingId = bookingRepository.save(booking).getId();

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText4");

        CommentDtoResponse commentDtoResponse = itemService.createComment(itemId, userId, commentDto);

        assertEquals(bookingId, bookingService.getBookingsByItemId(itemId).getFirst().getId());
    }

    @Test
    void testCreateBooking() {
        User user1 = new User();
        user1.setName("User13");
        user1.setEmail("user13@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item13");
        itemDto.setDescription("Description13");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User14");
        user2.setEmail("user14@user.com");
        Long userId2 = userRepository.save(user2).getId();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription5");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        BookingDtoResponse bookingDtoResponse = bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText5");

        CommentDtoResponse commentDtoResponse = itemService.createComment(itemId, userId, commentDto);

        assertEquals(bookingDtoResponse.getId(), bookingRepository.findById(bookingDtoResponse.getId()).orElseThrow().getId());
        assertEquals(bookingDtoResponse.getItem().getId(), bookingRepository.findById(bookingDtoResponse.getId()).orElseThrow().getItem().getId());
        assertEquals(bookingDtoResponse.getBooker(), bookingRepository.findById(bookingDtoResponse.getId()).orElseThrow().getBooker());
    }

    @Test
    void testUpdateBooking() {
        User user1 = new User();
        user1.setName("User15");
        user1.setEmail("user15@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item15");
        itemDto.setDescription("Description15");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User16");
        user2.setEmail("user16@user.com");
        Long userId2 = userRepository.save(user2).getId();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription7");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        BookingDtoResponse bookingDtoResponse = bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText7");

        CommentDtoResponse commentDtoResponse = itemService.createComment(itemId, userId, commentDto);

        BookingDtoResponse bookingDtoResponse2 = bookingService.updateBooking(userId, bookingDtoResponse.getId(), true);

        assertEquals(bookingDtoResponse2.getStatus(), bookingService.getBookingById(userId, bookingDtoResponse.getId()).getStatus());
    }

    @Test
    void testGetByBookerById() {
        User user1 = new User();
        user1.setName("User16");
        user1.setEmail("user16@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item16");
        itemDto.setDescription("Description16");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User17");
        user2.setEmail("user17@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription8");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText8");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDto> bookingDtoList = bookingService.getByBookerById(userId);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void testGetBookingsByUserIdAndStatus() {
        User user1 = new User();
        user1.setName("User17");
        user1.setEmail("user17@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item17");
        itemDto.setDescription("Description17");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User18");
        user2.setEmail("user18@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription9");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText9");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserIdAndStatus(userId, SearchType.ALL);

        assertEquals(1, bookingDtoResponseList.size());
    }

    @Test
    void testGetBookingsByUserIdAndStatusPast() {
        User user1 = new User();
        user1.setName("User177");
        user1.setEmail("user177@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item177");
        itemDto.setDescription("Description177");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User187");
        user2.setEmail("user187@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription97");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText97");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserIdAndStatus(userId, SearchType.PAST);

        assertEquals(1, bookingDtoResponseList.size());
    }

    @Test
    void testGetBookingsByUserIdAndStatusCurrent() {
        User user1 = new User();
        user1.setName("User178");
        user1.setEmail("user178@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item178");
        itemDto.setDescription("Description178");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User188");
        user2.setEmail("user188@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription98");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText98");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserIdAndStatus(userId, SearchType.CURRENT);

        assertEquals(0, bookingDtoResponseList.size());
    }

    @Test
    void testGetBookingsByUserIdAndStatusWaiting() {
        User user1 = new User();
        user1.setName("User17811");
        user1.setEmail("user17811@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item17811");
        itemDto.setDescription("Description17811");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User18811");
        user2.setEmail("user18811@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription9811");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText9811");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserIdAndStatus(userId, SearchType.WAITING);

        assertEquals(1, bookingDtoResponseList.size());
    }

    @Test
    void testGetBookingsByUserIdAndStatusFuture() {
        User user1 = new User();
        user1.setName("User1781");
        user1.setEmail("user1781@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item1781");
        itemDto.setDescription("Description1781");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User1881");
        user2.setEmail("user1881@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription981");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText981");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserIdAndStatus(userId, SearchType.FUTURE);

        assertEquals(0, bookingDtoResponseList.size());
    }


    @Test
    void testGetBookingsByUserItemsAndStatus() {
        User user1 = new User();
        user1.setName("User18");
        user1.setEmail("user18@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item18");
        itemDto.setDescription("Description18");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User19");
        user2.setEmail("user19@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription10");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText10");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserItemsAndStatus(userId, SearchType.ALL);

        assertEquals(1, bookingDtoResponseList.size());
    }

    @Test
    void testGetBookingsByUserItemsAndStatusPast() {
        User user1 = new User();
        user1.setName("User19");
        user1.setEmail("user19@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item19");
        itemDto.setDescription("Description19");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User20");
        user2.setEmail("user20@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription11");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText11");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserItemsAndStatus(userId, SearchType.PAST);

        assertEquals(1, bookingDtoResponseList.size());
    }

    @Test
    void testGetBookingsByUserItemsAndStatusCurrent() {
        User user1 = new User();
        user1.setName("User21");
        user1.setEmail("user21@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item19");
        itemDto.setDescription("Description19");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User22");
        user2.setEmail("user22@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription12");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText12");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserItemsAndStatus(userId, SearchType.CURRENT);

        assertEquals(0, bookingDtoResponseList.size());
    }

    @Test
    void testGetBookingsByUserItemsAndStatusFuture() {
        User user1 = new User();
        user1.setName("User23");
        user1.setEmail("user23@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item20");
        itemDto.setDescription("Description20");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User24");
        user2.setEmail("user24@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription13");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText13");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserItemsAndStatus(userId, SearchType.WAITING);

        assertEquals(0, bookingDtoResponseList.size());
    }

    @Test
    void testGetBookingsByUserItemsAndStatusWaiting() {
        User user1 = new User();
        user1.setName("User25");
        user1.setEmail("user25@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item21");
        itemDto.setDescription("Description21");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User26");
        user2.setEmail("user26@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription14");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText14");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserItemsAndStatus(userId, SearchType.REJECTED);

        assertEquals(0, bookingDtoResponseList.size());
    }

    @Test
    void testGetBookingsByUserItemsAndStatusRejected() {
        User user1 = new User();
        user1.setName("User27");
        user1.setEmail("user27@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item22");
        itemDto.setDescription("Description22");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User28");
        user2.setEmail("user28@user.com");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription15");

        itemRequestRepository.save(itemRequest);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemRepository.findById(itemId).orElseThrow().getId());
        bookingDto.setBooker(userRepository.findById(userId).orElseThrow());
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStart(LocalDateTime.now().minusHours(5));
        bookingDto.setEnd(LocalDateTime.now());

        bookingService.createBooking(userId, bookingDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText15");

        itemService.createComment(itemId, userId, commentDto);

        List<BookingDtoResponse> bookingDtoResponseList = bookingService.getBookingsByUserItemsAndStatus(userId, SearchType.FUTURE);

        assertEquals(0, bookingDtoResponseList.size());
    }
}
