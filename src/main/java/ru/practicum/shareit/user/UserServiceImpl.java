package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.dto.UserReadDto;
import ru.practicum.shareit.user.dto.UserWriteDto;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserReadDto getUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            log.info("Пользователя с id = {} нет", id);
            throw new NotFoundException("Пользователя с таким id нет");
        }
        return UserDtoMapper.userToUserReadDto(userRepository.findById(id).get());
    }

    @Override
    public UserReadDto saveUser(UserWriteDto userWriteDto) {
        User user = UserDtoMapper.userWriteDtoToUser(userWriteDto);
        if (userRepository.existsByEmail(userWriteDto.getEmail())) {
            log.info("Пользователь с таким email уже есть");
            throw new ValidationException("Пользователь с таким email уже есть", HttpStatus.CONFLICT);
        }
        return UserDtoMapper.userToUserReadDto(userRepository.save(user));
    }

    @Override
    public UserReadDto updateUser(UserWriteDto userWriteDto, Integer id) {
        if (!userRepository.existsById(id)) {
            log.info("Пользователя с id = {} нет", id);
            throw new NotFoundException("Пользователя с таким id нет");
        } else if (userRepository.existsByEmail(userWriteDto.getEmail())) {
            log.info("Пользователь с таким email уже есть");
            throw new ValidationException("Пользователь с таким email уже есть", HttpStatus.CONFLICT);
        }
        User user = userRepository.findById(id).get();
        if (userWriteDto.getEmail() != null) {
            user.setEmail(userWriteDto.getEmail());
        }
        if (userWriteDto.getName() != null) {
            user.setName(userWriteDto.getName());
        }
        userRepository.save(user);
        return UserDtoMapper.userToUserReadDto(user);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            log.info("Пользователя с id = {} нет", id);
            throw new NotFoundException("Пользователя с таким id нет");
        }
        userRepository.deleteById(id);
    }
}
