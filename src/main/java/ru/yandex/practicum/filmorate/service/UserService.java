package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private  final JdbcTemplate jdbcTemplate;
    long id = 0;

    public List<User> findAll() {
/*        return  jdbcTemplate.query("select * from users",(rs, rowNum) ->  new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("login"),
                rs.getString("email"),
                rs.getDate("birthday").toLocalDate()
        ));

        //return userStorage.getUsers();*/
        return   jdbcTemplate.query("select * from users",(rs, rowNum) ->   User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build());
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {       //если имя не задано, то name=login
            user.setName(user.getLogin());
        }
        user.setId(++id);
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
        /*if (!userStorage.getUsersMap().containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь № %d не найден", id));
        }*/
        User user = jdbcTemplate.queryForObject("select * from users where id = ?",new RowMapper<User>(){
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user1 = new User();
                user1.setId(rs.getLong("id"));
                user1.setName(rs.getString("name"));
                user1.setEmail(rs.getString("email"));
                user1.setLogin(rs.getString("login"));
                user1.setBirthday(rs.getDate("birthday").toLocalDate());

                return user1;
            }
        },id);
                return user;
            }




    public void addFriend(long id, long friendId) {
        if (friendId <= 0 || id <= 0) {
            throw new NotFoundException(String.format("Некорректный ID № %d ", id));
        }
        if (checkInList(id) && checkInList(friendId)) {
            userStorage.getUserById(id).getFriends().add(friendId);
            userStorage.getUserById(friendId).getFriends().add(id);
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
            userStorage.getUserById(id).getFriends().remove(friendId);
            userStorage.getUserById(friendId).getFriends().remove(id);
            log.info("Удалили из друзей   пользователя с ID {},пользователя c ID {}  ", id, friendId);
        } else {
            throw new NotFoundException(String.format("Пользователь № %d не найден", id));
        }
    }

    public List<User> getUserFriends(long id) {
        log.info("Вернули список друзей   пользователя с ID {}", id);
        return userStorage.getUsers().stream()
                .filter(o -> userStorage.getUserById(id).getFriends().contains(o.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {     // Находим общих друзей, и складываем в List
        if (checkInList(id) || checkInList(otherId)) {
            List<User> list = userStorage.getUsers().stream()
                    .filter(x -> userStorage.getUsersMap().get(id).getFriends()
                            .contains(x.getId())).filter(o -> userStorage.getUsersMap().get(otherId).getFriends()
                            .contains(o.getId())).collect(Collectors.toList());
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