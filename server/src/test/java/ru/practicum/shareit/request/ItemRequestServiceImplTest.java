package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ItemRequestServiceImplTest {
    @Autowired
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testGetItemRequests() {
        User user = new User();
        user.setName("User20");
        user.setEmail("user20@user.com");
        userRepository.save(user);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user);
        itemRequest.setDescription("ItemRequestDescription11");

        itemRequestRepository.save(itemRequest);

        assertEquals(1, itemRequestService.getItemRequests().size());
    }

    @Test
    void testCreateItemRequest() {
        User user = new User();
        user.setName("User21");
        user.setEmail("user21@user.com");
        Long userId = userRepository.save(user).getId();

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("ItemRequestDescription12");

        ItemRequestDtoResponse itemRequestDtoResponse = itemRequestService.createItemRequest(userId, itemRequestDto);

        assertEquals("ItemRequestDescription12", itemRequestDtoResponse.getDescription());
    }

    @Test
    void testGetItemRequestById() {
        User user = new User();
        user.setName("User22");
        user.setEmail("user22@user.com");
        Long userId = userRepository.save(user).getId();

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("ItemRequestDescription13");

        ItemRequestDtoResponse itemRequestDtoResponse = itemRequestService.createItemRequest(userId, itemRequestDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item88");
        itemDto.setDescription("Description88");
        itemDto.setOwner(user);
        itemDto.setAvailable(true);
        itemDto.setRequestId(itemRequestDtoResponse.getId());

        itemService.createItem(userId, itemDto);

        assertEquals(itemRequestDtoResponse.getId(), itemRequestService.getItemRequestById(itemRequestDtoResponse.getId()).getId());
        assertEquals("ItemRequestDescription13", itemRequestDtoResponse.getDescription());
    }

    @Test
    void testGetItemRequestsByUserId() {
        User user = new User();
        user.setName("User23");
        user.setEmail("user23@user.com");
        Long userId = userRepository.save(user).getId();

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("ItemRequestDescription14");

        ItemRequestDtoResponse itemRequestDtoResponse = itemRequestService.createItemRequest(userId, itemRequestDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item99");
        itemDto.setDescription("Description99");
        itemDto.setOwner(user);
        itemDto.setAvailable(true);
        itemDto.setRequestId(itemRequestDtoResponse.getId());

        itemService.createItem(userId, itemDto);

        assertEquals(itemRequestDtoResponse.getId(), itemRequestService.getItemRequestsByUserId(userId).getFirst().getId());
        assertEquals("ItemRequestDescription14", itemRequestService.getItemRequestsByUserId(userId).getFirst().getDescription());
    }
}
