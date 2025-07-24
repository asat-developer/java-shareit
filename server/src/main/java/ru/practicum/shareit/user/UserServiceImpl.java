package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserWriteDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.dto.UserReadDto;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserReadDto getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователя с таким id нет"));
        return UserDtoMapper.userToUserReadDto(user);
    }

    @Override
    public UserReadDto saveUser(UserWriteDto userWriteDto) {
        User user = UserDtoMapper.userWriteDtoToUser(userWriteDto);
        if (userRepository.existsByEmail(userWriteDto.getEmail())) {
            throw new ValidationException("Пользователь с таким email уже есть", HttpStatus.CONFLICT);
        }
        userRepository.save(user);
        return UserDtoMapper.userToUserReadDto(user);
    }

    @Override
    public UserReadDto updateUser(UserWriteDto userWriteDto, Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Нет таково пользователя !"));
        if (userRepository.existsByEmail(userWriteDto.getEmail())) {
            throw new ValidationException("Пользователь с таким email уже есть", HttpStatus.CONFLICT);
        }
        if (userWriteDto.getEmail() != null) {
            user.setEmail(userWriteDto.getEmail());
        }
        if (userWriteDto.getName() != null) {
            user.setName(userWriteDto.getName());
        }
        return UserDtoMapper.userToUserReadDto(user);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователя с таким id нет");
        }
        userRepository.deleteById(id);
    }
}
