package ru.practicum.shareit.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserWriteDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") @Positive @NotNull Integer userId) {
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@Validated(UserWriteDto.OnPost.class)
                                               @RequestBody UserWriteDto userWriteDto) {
        return userClient.saveUser(userWriteDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
                                  @PathVariable("userId") @Positive @NotNull Integer userId,
                                             @Validated(UserWriteDto.OnPatch.class)
                                             @RequestBody UserWriteDto userWriteDto) {
        return userClient.updateUser(userId, userWriteDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") @Positive @NotNull Integer userId) {
        return userClient.deleteUser(userId);
    }
}
