package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Component
public class UserDtoMapper {

    public User userWriteDtoToUser(UserWriteDto userWriteDto) {
        return new User(null, userWriteDto.getEmail(), userWriteDto.getName());
    }

    public UserReadDto userToUserReadDto(User user) {
        return new UserReadDto(user.getId(), user.getEmail(), user.getName());
    }
}
