package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public void create(User user) {
        users.put(user.getId(), user);
    }

    public void update(User user) {
        users.put(user.getId(), user);
    }


    public void delete(User user) {
        users.remove(user.getId());
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
    public Map<Long, User> getMapUsers() {
        return users;
    }

    public User getUserById(long id) {

        return users.get(id);
    }

    public Map<Long, User> getUsersMap() {
        return users;
    }


}
