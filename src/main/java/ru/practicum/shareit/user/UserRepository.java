package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    User getUserById(Integer id);

    User saveUser(User user);

    User updateUser(User user, Integer id);

    void deleteUser(Integer id);

    boolean checkUser(Integer id);

    boolean checkEmailCrosses(User user);
}
