package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
    private Map<Long, User> users = new HashMap<>();
    private long idCount = 0;
    private final UserMapper userMapper;

    public List<UserDto> getUsers() {
        log.info("Запустили метод получения всех пользователей в репозитории");
        List<User> usersList = users.values().stream().toList();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : usersList) {
            userDtoList.add(userMapper.mapToUserDto(user));
        }
        log.info("Собрали всех пользователей в список в репозитории");
        return userDtoList;
    }

    public UserDto getUserById(Long userId) {
        log.info("Запустили метод получения пользователя по айди в репозитории");
        return userMapper.mapToUserDto(users.get(userId));
    }

    public User getUserByIdNotDto(Long userId) {
        log.info("Запустили метод получения пользователя по айди в репозитории");
        return users.get(userId);
    }

    public UserDto createUser(UserDto userDto) {
        log.info("Запустили метод создания пользователя в репозитории");
        User user = userMapper.mapToUser(userDto);
        idCount = idCount + 1;
        user.setId(idCount);
        users.put(idCount, user);
        log.info("Сохранили нового пользователя в репозитории");
        return userMapper.mapToUserDto(user);
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Запустили метод обновления пользователя в репозитории");
        User newUser = userMapper.mapToUser(userDto);

        User oldUser = users.get(userId);
        if (userDto.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        users.put(userId, oldUser);
        log.info("Обновили пользователя в репозитории");
        return userMapper.mapToUserDto(oldUser);
    }

    public void deleteUser(Long userId) {
        log.info("Запустили метод удаления пользователя в репозитории");
        users.remove(userId);
        log.info("Удалили пользователя в репозитории");
    }

    public Optional<User> findByEmail(String email) {
        log.info("Запустили метод поиска пользователя по емэйлу в репозитории");
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return Optional.ofNullable(user);
            }
        }
        return Optional.empty();
    }
}
