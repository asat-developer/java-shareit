package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserDtoMapper {

    public static User userWriteDtoToUser(UserWriteDto userWriteDto) {
        User user = new User();
        user.setName(userWriteDto.getName());
        user.setEmail(userWriteDto.getEmail());
        return user;
    }

    public static UserReadDto userToUserReadDto(User user) {
        return new UserReadDto(user.getId(), user.getEmail(), user.getName());
    }
}
