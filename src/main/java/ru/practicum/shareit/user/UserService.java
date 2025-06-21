package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserReadDto;
import ru.practicum.shareit.user.dto.UserWriteDto;

public interface UserService {
    UserReadDto getUserById(Integer id);

    UserReadDto saveUser(UserWriteDto userWriteDto);

    UserReadDto updateUser(UserWriteDto userWriteDto, Integer id);

    void deleteUser(Integer id);
}
