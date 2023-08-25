package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Primary
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Map<String, String> params = Map.of("name", user.getName(),
                 "login", user.getLogin(), "email", user.getEmail(),
                 "birthday", user.getBirthday().toString()
        );
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId((Long) id);
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE USERS " +
                "SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? " +
                "WHERE ID=?;";        // текст SQL запроса
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
    }

    @Override
    public void delete(User user) {
        String sql = "DELETE FROM users" +
                "WHERE id = ?;";        // текст SQL запроса
        jdbcTemplate.update(sql, user.getId());
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT u.id, u.name , u.email, u.login, u.birthday" +
                " FROM users u LEFT JOIN friends AS f ON u.id = f.user_id GROUP BY u.id;";
        return jdbcTemplate.query(sql, userRowMapper());

    }

    @Override
    public User getUserById(long id) {
        List<User> users = jdbcTemplate.query("select * from users where id = ?", userRowMapper(), id);
        if (users.size() != 1) {
            throw new RuntimeException("Пользователь с id = " + id + " не найден");
        }
        return users.get(0);
    }

    @Override
    public Map<Long, User> getUsersMap() {
        return getUsers().stream().collect(Collectors.toMap(User::getId, user -> user));
    }

    @Override
    public List<User> getFriends(long id) {
        String sql =
                "SELECT * FROM users " +
                        " WHERE id IN " +
                        " (SELECT friend_id FROM FRIENDS WHERE user_id = ? );";

        List<User> users = jdbcTemplate.query(sql, userRowMapper(), id);
        //System.out.println("СПИСОК =" + users);
        return users;
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        String sql = "SELECT * FROM USERS WHERE ID IN( " +      //все поля из пользователя
                "SELECT DISTINCT(FRIEND_ID) FROM FRIENDS WHERE USER_ID = ? " +      //уникальные данные из друзей
                " AND FRIEND_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ? )" +
                " );";

        List<User> users = jdbcTemplate.query(sql, userRowMapper(), id, otherId);
        return users;
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setLogin(rs.getString("login"));
            user.setEmail(rs.getString("email"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        };

    }
/*    public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }*/
}
