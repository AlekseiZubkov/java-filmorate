package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserStorage userStorage;
    private User userOne;
    private User user2;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        userOne = User.builder()
                .name("OneName")
                .login("OneLog")
                .email("One@mail.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user2 = User.builder()
                .name("twoName")
                .login("twoLog")
                .email("two@mail.ru")
                .birthday(LocalDate.of(2005, 5, 5))
                .build();
        String sql =
                "delete from users;\n" +
                        "delete from friends;\n" +
                        "delete from films;\n" +
                        "delete from genre;\n" +
                        "delete from likes;\n" +
                        "delete from films_genre;";
        jdbcTemplate.update(sql);
    }


    @Test
    void createUser() {
        userStorage.create(userOne);
        assertEquals("OneName", userOne.getName(), "Имя некорректно");
        assertEquals("OneLog", userOne.getLogin(), "Логин некорректен");
        assertEquals("One@mail.ru", userOne.getEmail(), "Email некорректен");
        assertEquals(LocalDate.of(2000, 1, 1), userOne.getBirthday(), "Дата рождения неверна");
    }

    @Test
    void updateUser() {

        userStorage.create(userOne);
        assertEquals("OneName", userOne.getName(), "Имя не корректно");
        userOne.setName("OneNameUpdate");
        userOne.setLogin("OneLogUpdate");
        userOne.setEmail("OneUpdate@mail.ru");
        userOne.setBirthday(LocalDate.of(2000, 2, 2));
        userOne.setId(1);
        userStorage.update(userOne);
        user2 = userStorage.getUserById(1);
        assertEquals("OneNameUpdate", user2.getName(), "Имя некорректно");
        assertEquals("OneLogUpdate", user2.getLogin(), "Логин некорректен");
        assertEquals("OneUpdate@mail.ru", user2.getEmail(), "Email некорректен");
        assertEquals(LocalDate.of(2000, 2, 2), user2.getBirthday(), "Дата рождения неверна");
    }

    @Test
    void deleteUser() {
        Map<Long, User> mapUser = userStorage.getUsersMap();
        assertEquals(0, userStorage.getUsersMap().size(), "Неверное кол-во пользователей");
        userStorage.create(userOne);
        mapUser = userStorage.getUsersMap();
        assertFalse(mapUser.isEmpty(), "Неверное кол-во пользователей");
        userStorage.delete(userOne);
        mapUser = userStorage.getUsersMap();
        assertTrue(mapUser.isEmpty(), "Неверное кол-во пользователей");

    }

    @Test
    void getUser() {
        userStorage.create(userOne);
        userStorage.create(user2);
        List<User> list = userStorage.getUsers();
        assertEquals(2, list.size(), "Неверное кол-во пользователей");
    }
}