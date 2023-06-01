package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
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
        if (user.getName() == null || user.getName().isEmpty()) {       //если имя не задано, то name=login
            user.setName(user.getLogin());
        }
        if (user.getFriends() == null) {        //если списка нет, то присвоить пустой список
            user.setFriends(new HashSet<>());
        }
        user.setId(++id);
        userStorage.create(user);
        return user;
    }

    public void updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (checkInList(user.getId())) {
            userStorage.update(user);
        } else {
            throw new ValidationException("Нет такого пользователя в списке");
        }
    }

    public void deleteUser(User user) {
        userStorage.delete(user);
    }

    public User getUserById(long id) {
        return userStorage.getUsers().stream()
                .filter(u -> u.getId()==id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
    }


    public void addFriend(long id, long friendId) {
        if (checkInList(id) && checkInList(id)){
            userStorage.getUsersMap().get(id).getFriends().add(friendId);
            userStorage.getUsersMap().get(friendId).getFriends().add(id);
        }
    }

    public void deleteFriend(long id, long friendId) {
        userStorage.getUsersMap().get(id).getFriends().remove(friendId);
        userStorage.getUsersMap().get(friendId).getFriends().remove(id);
    }

    public List<User> getUserFriends(long id) {
            return userStorage.getUsers().stream()
                    .filter(o -> userStorage.getUserById(id).getFriends().contains(o.getId()))
                    .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {     // Находим общих друзей, и складываем в List
        return userStorage.getUsers().stream()
                .filter(x -> userStorage.getUsersMap().get(id).getFriends()
                .contains(x.getId())).filter(o -> userStorage.getUsersMap().get(otherId).getFriends()
                .contains(o.getId())).collect(Collectors.toList());
    }
    private boolean checkInList (long id){      //Проверка, что пользователь с таким ID есть в списке
        if (userStorage.getUsersMap().containsKey(id)) {
            return true;
        } else {
            throw new UserNotFoundException(String.format("Пост № %d не найден", id));
        }
    }

}