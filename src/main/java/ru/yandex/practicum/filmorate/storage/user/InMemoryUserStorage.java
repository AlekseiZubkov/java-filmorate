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

//      /*  User user1 =  jdbcTemplate.queryForObject("select * from users where id = ?", new RowMapper<User>() {
//            @Override
//            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                return new User(rs.getLong("id"),rs.getString("EMAIL")
//                        ,rs.getString("login"),rs.getString("name")
//                        , rs.getDate("birthday").toLocalDate(),id);
//                /*User(rs.getLong("id"),rs.getString("EMAIL")
//                        ,rs.getString("login"),rs.getString("name")
//                        ,rs.getDate("birthday"));
//            }
//        }, id);
//        return user1;*/
//
//        log.info("3 - Пришел Get запрос /users/{}",id);
//        List<User> users = jdbcTemplate.query("select * from users where id = ?",(rs, rowNum) -> new User(
//                rs.getLong("id"),
//                rs.getString("email"),
//                rs.getString("login"),
//                rs.getString("name"),
//                rs.getDate("birthday").toLocalDate()
//        ),id);
//
//        if (users.size() != 1) {
//            throw new RuntimeException("Пользователь с id = " + id + " не найден");
//        }
//        return users.get(0);
            return users.get(id);
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
