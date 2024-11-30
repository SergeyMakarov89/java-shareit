package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserMapper {
    public User mapToUser(UserDto request) {
        log.info("Запустили метод корвертации пользователя из юзердто в юзера в юзермаппере");
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return user;
    }

    public UserDto mapToUserDto(User user) {
        log.info("Запустили метод корвертации пользователя из юзера в юзердто в юзермаппере");
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }
}
