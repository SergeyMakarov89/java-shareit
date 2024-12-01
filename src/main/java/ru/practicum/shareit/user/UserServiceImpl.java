package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.SameDataException;
import ru.practicum.shareit.exeption.ValidationException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        log.info("Запустили метод получения всех пользователей в сёрвисе");
        return userRepository.getUsers();
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Запустили метод получения пользователя по айди в сёрвисе");
        return userRepository.getUserById(userId);
    }

    @Override
    public UserDto createUser(UserDto request) {
        log.info("Запустили метод создания пользователя в сёрвисе");
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ValidationException("Имейл должен быть указан");
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ValidationException("Имя пользователя должно быть указано");
        }

        Optional<User> alreadyExistUser = userRepository.findByEmail(request.getEmail());
        if (alreadyExistUser.isPresent()) {
            throw new SameDataException("Данный имейл уже используется");
        }
        return userRepository.createUser(request);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto request) {
        log.info("Запустили метод обновления пользователя в сёрвисе");

        Optional<User> alreadyExistUser = userRepository.findByEmail(request.getEmail());
        if (alreadyExistUser.isPresent()) {
            throw new SameDataException("Данный имейл уже используется");
        }

        return userRepository.updateUser(userId, request);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Запустили метод удаления пользователя в сёрвисе");
        userRepository.deleteUser(userId);
    }
}
