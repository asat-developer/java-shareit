package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserWriteDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") int userId) {
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@Validated(UserWriteDto.OnPost.class)
                                               @RequestBody UserWriteDto userWriteDto) {
        return userClient.saveUser(userWriteDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Validated(UserWriteDto.OnPatch.class)
                                  @PathVariable("userId") int userId,
                                  @RequestBody UserWriteDto userWriteDto) {
        return userClient.updateUser(userId, userWriteDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") int userId) {
        return userClient.deleteUser(userId);
    }
}
