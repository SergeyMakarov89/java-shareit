package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentDtoResponse;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ItemServiceImplTest {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
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
    void testGetItems() {
        User user = new User();
        user.setName("User1");
        user.setEmail("user1@user.com");
        Long userId = userRepository.save(user).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item1");
        itemDto.setDescription("Description1");
        itemDto.setOwner(user);
        itemDto.setAvailable(true);

        itemService.createItem(userId, itemDto);
        assertFalse(itemService.getItems().isEmpty());
        assertEquals(1, itemService.getItems().size());
    }

    @Test
    void testGetItemById() {
        User user = new User();
        user.setName("User2");
        user.setEmail("user2@user.com");
        Long userId = userRepository.save(user).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item2");
        itemDto.setDescription("Description2");
        itemDto.setOwner(user);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();
        assertEquals(itemId, itemService.getItemById(itemId).getId());
        assertEquals("Item2", itemService.getItemById(itemId).getName());
        assertEquals("Description2", itemService.getItemById(itemId).getDescription());
    }

    @Test
    void testGetItemsByUserId() {
        User user = new User();
        user.setName("User3");
        user.setEmail("user3@user.com");
        Long userId = userRepository.save(user).getId();

        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Item3");
        itemDto1.setDescription("Description3");
        itemDto1.setOwner(user);
        itemDto1.setAvailable(true);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Item4");
        itemDto2.setDescription("Description4");
        itemDto2.setOwner(user);
        itemDto2.setAvailable(true);

        itemService.createItem(userId, itemDto1);
        itemService.createItem(userId, itemDto2);
        assertEquals(2, itemService.getItemsByUserId(userId).size());
    }

    @Test
    void testCreateItem() {
        User user = new User();
        user.setName("User4");
        user.setEmail("user4@user.com");
        Long userId = userRepository.save(user).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item5");
        itemDto.setDescription("Description5");
        itemDto.setOwner(user);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();
        assertEquals(itemId, itemService.getItemById(itemId).getId());
        assertEquals("Item5", itemService.getItemById(itemId).getName());
        assertEquals("Description5", itemService.getItemById(itemId).getDescription());
        assertEquals(userId, itemService.getItemById(itemId).getOwner().getId());
    }

    @Test
    void testUpdateItem() {
        User user = new User();
        user.setName("User5");
        user.setEmail("user5@user.com");
        Long userId = userRepository.save(user).getId();

        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Item6");
        itemDto1.setDescription("Description6");
        itemDto1.setOwner(user);
        itemDto1.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto1).getId();

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Item7");
        itemDto2.setDescription("Description7");
        itemDto2.setOwner(user);
        itemDto2.setAvailable(true);

        itemService.updateItem(userId, itemId, itemDto2);

        assertEquals(itemId, itemService.getItemById(itemId).getId());
        assertEquals("Item7", itemService.getItemById(itemId).getName());
        assertEquals("Description7", itemService.getItemById(itemId).getDescription());
    }

    @Test
    void testDeleteItem() {
        Integer idCountsBeforeDeleting = itemService.getItems().size();
        User user = new User();
        user.setName("User6");
        user.setEmail("user6@user.com");
        Long userId = userRepository.save(user).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item7");
        itemDto.setDescription("Description7");
        itemDto.setOwner(user);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        itemService.deleteItem(itemId);

        assertEquals(idCountsBeforeDeleting, itemService.getItems().size());
    }

    @Test
    void testFindItems() {
        User user = new User();
        user.setName("User7");
        user.setEmail("user7@user.com");
        Long userId = userRepository.save(user).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item8");
        itemDto.setDescription("Description8");
        itemDto.setOwner(user);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        Long itemCount = (long) itemService.getItems().size();

        assertEquals(itemCount, itemService.findItems("Description").size());
    }

    @Test
    void testCreateComment() {
        User user1 = new User();
        user1.setName("User8");
        user1.setEmail("user8@user.com");
        Long userId = userRepository.save(user1).getId();

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item9");
        itemDto.setDescription("Description9");
        itemDto.setOwner(user1);
        itemDto.setAvailable(true);

        Long itemId = itemService.createItem(userId, itemDto).getId();

        User user2 = new User();
        user2.setName("User9");
        user2.setEmail("user9@user.com");
        Long userId2 = userRepository.save(user2).getId();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user2);
        itemRequest.setDescription("ItemRequestDescription1");

        itemRequestRepository.save(itemRequest);

        Booking booking = new Booking();
        booking.setItem(itemRepository.findById(itemId).orElseThrow());
        booking.setBooker(userRepository.findById(userId).orElseThrow());
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusHours(5));
        booking.setEnd(LocalDateTime.now());
        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText1");

        CommentDtoResponse commentDtoResponse = itemService.createComment(itemId, userId, commentDto);

        assertEquals(itemId, commentDtoResponse.getItem().getId());
        assertEquals("User8", commentDtoResponse.getAuthorName());
        assertEquals("CommentText1", commentDtoResponse.getText());
    }

    @Test
    void testThrowNotFoundExceptionByUser() {
        User user = new User();
        user.setName("User44");
        user.setEmail("user44@user.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item5");
        itemDto.setDescription("Description5");
        itemDto.setOwner(user);
        itemDto.setAvailable(true);

        assertThrows(NotFoundException.class, () -> itemService.createItem(1L, itemDto));
    }
}
