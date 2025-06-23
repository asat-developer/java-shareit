package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.dto.UserReadDto;
import ru.practicum.shareit.user.dto.UserWriteDto;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserReadDto getUserById(Integer id) {
        if (!userRepository.checkUser(id)) {
            log.info("Пользователя с id = {} нет", id);
            throw new NotFoundException("Пользователя с таким id нет");
        }
        return UserDtoMapper.userToUserReadDto(userRepository.getUserById(id));
    }

    @Override
    public UserReadDto saveUser(UserWriteDto userWriteDto) {
        User user = UserDtoMapper.userWriteDtoToUser(userWriteDto);
        if (userRepository.checkEmailCrosses(user)) {
            log.info("Пользователь с таким email уже есть");
            throw new ValidationException("Пользователь с таким email уже есть");
        }
        return UserDtoMapper.userToUserReadDto(userRepository.saveUser(user));
    }

    @Override
    public UserReadDto updateUser(UserWriteDto userWriteDto, Integer id) {
        User user = UserDtoMapper.userWriteDtoToUser(userWriteDto);
        if (!userRepository.checkUser(id)) {
            log.info("Пользователя с id = {} нет", id);
            throw new NotFoundException("Пользователя с таким id нет");
        } else if (userRepository.checkEmailCrosses(user)) {
            log.info("Пользователь с таким email уже есть");
            throw new ValidationException("Пользователь с таким email уже есть");
        }
        return UserDtoMapper.userToUserReadDto(userRepository.updateUser(user, id));
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.checkUser(id)) {
            log.info("Пользователя с id = {} нет", id);
            throw new NotFoundException("Пользователя с таким id нет");
        }
        userRepository.deleteUser(id);
    }
}
