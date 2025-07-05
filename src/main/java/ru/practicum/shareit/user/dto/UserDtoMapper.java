package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserDtoMapper {

    public static User userWriteDtoToUser(UserWriteDto userWriteDto) {
        return new User(null, userWriteDto.getEmail(), userWriteDto.getName());
    }

    public static UserReadDto userToUserReadDto(User user) {
        return new UserReadDto(user.getId(), user.getEmail(), user.getName());
    }
}
