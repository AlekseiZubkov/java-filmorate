package ru.yandex.practicum.filmorate.storage.user.friend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDbFriends implements UserFrendsStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbFriends(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(long id, long friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?);";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        String sql = "DELETE FROM friends\n" +
                "WHERE user_id=? AND friend_id=?;\n";
        jdbcTemplate.update(sql, id, friendId);
    }
}
