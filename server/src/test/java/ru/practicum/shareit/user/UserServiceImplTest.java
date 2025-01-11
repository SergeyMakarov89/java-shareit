package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.SameDataException;
import ru.practicum.shareit.exeption.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;

    @Test
    void testGetUsers() {
        UserDto userDto = new UserDto();
        userDto.setName("User1");
        userDto.setEmail("user1@user.com");
        userService.createUser(userDto).getId();
        assertFalse(userService.getUsers().isEmpty());
        assertEquals(1, userService.getUsers().size());
    }

    @Test
    void testGetUserById() {
        UserDto userDto = new UserDto();
        userDto.setName("User2");
        userDto.setEmail("user2@user.com");
        Long userId = userService.createUser(userDto).getId();
        assertEquals(userId, userService.getUserById(userId).getId());
        assertEquals("User2", userService.getUserById(userId).getName());
        assertEquals("user2@user.com", userService.getUserById(userId).getEmail());
    }

    @Test
    void testCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("User3");
        userDto.setEmail("user3@user.com");
        Long userId = userService.createUser(userDto).getId();

        assertEquals("User3", userService.getUserById(userId).getName());
        assertEquals("user3@user.com", userService.getUserById(userId).getEmail());
    }

    @Test
    void testUpdateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("User4");
        userDto.setEmail("user4@user.com");
        Long userId = userService.createUser(userDto).getId();
        UserDto userDto2 = new UserDto();
        userDto2.setName("User44");
        userDto2.setEmail("user44@user.com");
        userService.updateUser(userId, userDto2);

        assertEquals("User44", userService.getUserById(userId).getName());
        assertEquals("user44@user.com", userService.getUserById(userId).getEmail());
    }

    @Test
    void testDeleteUser() {
        Integer idCountsBeforeDeleting = userService.getUsers().size();
        UserDto userDto = new UserDto();
        userDto.setName("User5");
        userDto.setEmail("user5@user.com");
        Long userId = userService.createUser(userDto).getId();
        userService.deleteUser(userId);

        assertEquals(idCountsBeforeDeleting, userService.getUsers().size());
    }

    @Test
    void testThrowValidationExceptionWithEmptyEmail() {
        UserDto userDto = new UserDto();
        userDto.setName("User66");
        userDto.setEmail("");

        assertThrows(ValidationException.class, () -> userService.createUser(userDto));
    }

    @Test
    void testThrowSameDataExceptionWithEmail() {
        UserDto userDto1 = new UserDto();
        userDto1.setName("User77");
        userDto1.setEmail("user77@user.com");
        userService.createUser(userDto1);

        UserDto userDto2 = new UserDto();
        userDto2.setName("User88");
        userDto2.setEmail("user77@user.com");

        assertThrows(SameDataException.class, () -> userService.createUser(userDto2));
    }
}
