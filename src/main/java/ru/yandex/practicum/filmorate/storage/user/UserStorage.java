package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    void create(User user);

    void update(User user);

    void delete(User user);

    List<User> getUsers();

    User getUserById(long id);

    Map<Long, User> getUsersMap();

    List<User> getFriends(long id);

    List<User> getCommonFriends(long id, long otherId);



}
