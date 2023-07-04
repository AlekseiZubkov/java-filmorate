package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserDbStorage implements UserStorage {
    private JdbcTemplate jdbcTemplate;
    @Override
    public void create(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public List<User> getUsers() {
        return   jdbcTemplate.query("select * from users",(rs, rowNum) ->   User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build());
    }

    @Override
    public User getUserById(long id) {
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

    @Override
    public Map<Long, User> getUsersMap() {
        return null;
    }

}
