package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.friend.UserFrendsStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final UserFrendsStorage userFrendsStorage;

    long id = 0;

    public List<User> findAll() {
        return userStorage.getUsers();
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {       //если имя не задано, то name=login
            user.setName(user.getLogin());
        }
        //user.setId(++id);
        userStorage.create(user);
        log.info("Создали пользователя: {}", user);
        return user;
    }

    public void updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (checkInList(user.getId())) {
            userStorage.update(user);
            log.info("Обновили пользователя: {}", user);
        } else {
            throw new NotFoundException("Нет такого пользователя в списке");
        }
    }

    public User getUserById(long id) {
        if (!userStorage.getUsersMap().containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь № %d не найден", id));
        }
        return userStorage.getUserById(id);
    }


    public void addFriend(long id, long friendId) {
        if (friendId <= 0 || id <= 0) {
            throw new NotFoundException(String.format("Некорректный ID № %d ", id));
        }
        if (checkInList(id) && checkInList(friendId)) {
            userFrendsStorage.addFriend(id, friendId);
            log.info("Добавили в друзья  к пользователю с ID {},пользователя c ID {}  ", id, friendId);
        } else {
            throw new NotFoundException(String.format("Пользователь № %d не найден", id));
        }
    }

    public void deleteFriend(long id, long friendId) {
        if (friendId <= 0 || id <= 0) {
            throw new NotFoundException(String.format("Некорректный ID № %d ", id));
        }
        if (checkInList(id) && checkInList(friendId)) {
            userFrendsStorage.deleteFriend(id, friendId);
            log.info("Удалили из друзей   пользователя с ID {},пользователя c ID {}  ", id, friendId);
        } else {
            throw new NotFoundException(String.format("Пользователь № %d не найден", id));
        }
    }

    public List<User> getUserFriends(long id) {
        log.info("Вернули список друзей пользователя с ID {}", id);
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long id, long otherId) {     // Находим общих друзей, и складываем в List
        if (checkInList(id) || checkInList(otherId)) {
            List<User> list = userStorage.getCommonFriends(id, otherId);
            log.debug("Список общих друзей: {}", list);
            return list;
        } else {
            throw new NotFoundException(String.format("Пользователь № %d не найден", id));
        }
    }

    private boolean checkInList(long id) {      //Проверка, что пользователь с таким ID есть в списке

        return userStorage.getUsersMap().containsKey(id);
    }

}