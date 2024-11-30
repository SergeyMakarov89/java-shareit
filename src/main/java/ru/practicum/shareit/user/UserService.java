package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUserById(Long userId);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);
}
