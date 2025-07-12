/*package ru.practicum.shareit.user;

//import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public User saveUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User newUser, Integer id) {
        User user = users.get(id);
        if (newUser.getEmail() != null) {
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            user.setName(newUser.getName());
        }
        return user;
    }

    @Override
    public void deleteUser(Integer id) {
        users.remove(id);
    }

    @Override
    public boolean checkUser(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public boolean checkEmailCrosses(User user) {
        return users.values().stream()
                .map(user0 -> user0.getEmail())
                .anyMatch(email -> email.equals(user.getEmail()));
    }

    private Integer getNextId() {
        return users.keySet().stream()
                .max((o1, o2) -> Integer.compare(o1, o2))
                .orElse(0) + 1;
    }
}*/