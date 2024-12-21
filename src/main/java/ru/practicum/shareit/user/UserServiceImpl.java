package ru.practicum.shareit.user;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.SameDataException;
import ru.practicum.shareit.exeption.ValidationException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        log.info("Запустили метод получения всех пользователей в сёрвисе");
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            userDtoList.add(UserMapper.mapToUserDto(user));
        }
        return userDtoList;
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Запустили метод получения пользователя по айди в сёрвисе");
        User user = userRepository.findById(userId).get();
        return UserMapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto request) {
        log.info("Запустили метод создания пользователя в сёрвисе");
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ValidationException("Имейл должен быть указан");
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ValidationException("Имя пользователя должно быть указано");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new SameDataException("Данный имейл уже используется");
        }
        return UserMapper.mapToUserDto(userRepository.save(UserMapper.mapToUser(request)));
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto request) {
        log.info("Запустили метод обновления пользователя в сёрвисе");
        request.setId(userId);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new SameDataException("Данный имейл уже используется");
        }
        if (request.getEmail() == null) {
            request.setEmail(userRepository.findById(userId).get().getEmail());
        }
        if (request.getName() == null) {
            request.setName(userRepository.findById(userId).get().getName());
        }

        User user = userRepository.save(UserMapper.mapToUser(request));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Запустили метод удаления пользователя в сёрвисе");
        userRepository.deleteById(userId);
    }
}
