package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserReadDto;
import ru.practicum.shareit.user.dto.UserWriteDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserReadDto getUserById(@PathVariable("userId") Integer userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserReadDto saveUser(@RequestBody UserWriteDto userWriteDto) {
        return userService.saveUser(userWriteDto);
    }

    @PatchMapping("/{userId}")
    public UserReadDto updateUser(@PathVariable("userId") Integer userId,
                                  @RequestBody UserWriteDto userWriteDto) {
        return userService.updateUser(userWriteDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        userService.deleteUser(userId);
    }
}
