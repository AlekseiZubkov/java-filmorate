package ru.yandex.practicum.filmorate.storage.user;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private  final JdbcTemplate jdbcTemplate;

    public InMemoryUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    }

    public Map<Long, User> getUsersMap() {
        return users;
    }


//    private RowMapper<User> userRowMapper(){
//    return (rs, rowNum) -> new User(
//            rs.getLong("id"),
//            rs.getString("email"),
//            rs.getString("login"),
//            rs.getString("name"),
//            rs.getDate("birthday").toLocalDate()
//    );
  //  }

}
