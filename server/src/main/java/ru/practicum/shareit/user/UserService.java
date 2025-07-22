package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserWriteDto;
import ru.practicum.shareit.user.dto.UserReadDto;

public interface UserService {
    UserReadDto getUserById(Integer id);

    UserReadDto saveUser(UserWriteDto userWriteDto);

    UserReadDto updateUser(UserWriteDto userWriteDto, Integer id);

    void deleteUser(Integer id);
}
