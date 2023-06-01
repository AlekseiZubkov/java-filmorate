package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    long id = 0;

    public List<User> findAll() {
        return userStorage.getUsers();
    }
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        userStorage.create(user);
        return user;
    }

    public void updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (userStorage.getUsers().stream()
                .filter(user1 -> user1.getId() == user.getId())
                .findAny()
                .orElse(null) != null) {
            userStorage.update(user);
        } else {
            throw new ValidationException("Нет такого пользователя в списке");
        }
    }
    public void deleteUser(User user) {
        userStorage.delete(user);
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }



    public void addFriend(long id, long friendId) {
        userStorage.getUsersMap().get(id).getFriends().add(friendId);
        userStorage.getUsersMap().get(friendId).getFriends().add(id);
    }

    public void deleteFriend(long id, long friendId) {
        userStorage.getUserById(id).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(id);
    }

    public List<User> getUserFriends(long id) {
        return userStorage.getUsers().stream()
                .filter(x -> userStorage.getUserById(id).getFriends().contains(x.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        return userStorage.getUsers().stream().filter(x -> userStorage.getUserById(id).getFriends()
                .contains(x.getId())).filter(x -> userStorage.getUserById(otherId).getFriends()
                .contains(x.getId())).collect(Collectors.toList());
    }


}