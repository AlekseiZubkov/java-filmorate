package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public User getUserById(long id) {

        return users.get(id);
    }

    public Map<Long, User> getUsersMap() {
        return users;
    }

    @Override
    public List<User> getFriends(long id) {
        return getUsers().stream().filter(user -> getUsersMap().get(id).getFriends().contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        return getUsers().stream()
                .filter(user -> getUsersMap().get(id).getFriends().contains(user.getId())
                        && getUsersMap().get(otherId).getFriends().contains(user.getId()))
                .collect(Collectors.toList());
    }
}