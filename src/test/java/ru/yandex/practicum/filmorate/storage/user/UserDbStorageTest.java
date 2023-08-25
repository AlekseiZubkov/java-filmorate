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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserStorage userStorage;
    private User userOne;
    private User userSecond;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        userSecond = User.builder()
                .name("Sten")
                .login("stenLog")
                .email("stenLog@mail.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
    }

    void afterEach() {
        String sql =
                "delete from users;\n" +
                        "delete from friends;\n" +
                        "delete from films;\n" +
                        "delete from genre;\n" +
                        "delete from mpa;\n" +
                        "delete from likes;\n" +
                        "delete from films_genre;";
        jdbcTemplate.update(sql);
    }

    @Test
    void createUser() {
        userOne = User.builder()
                .name("OneName")
                .login("OneLog")
                .email("One@mail.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userStorage.create(userOne);
        assertEquals("OneName", userOne.getName(), "Имя не корректно");
        assertEquals("OneLog", userOne.getLogin(), "Логин не корректен");
        assertEquals("One@mail.ru", userOne.getEmail(), "Email не корректен");
        assertEquals(LocalDate.of(2000, 1, 1), userOne.getBirthday(), "Дата рождения не верна");
    }

    @Test
    void updateUser() {
        userOne = User.builder()
                .name("OneName")
                .login("OneLog")
                .email("One@mail.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userStorage.create(userOne);
        assertEquals("OneName", userOne.getName(), "Имя не корректно");
        userOne.setName("OneNameUpdate");
        userOne.setLogin("OneLogUpdate");
        userOne.setEmail("OneUpdate@mail.ru");
        userOne.setBirthday(LocalDate.of(2000, 2, 2));
        userOne.setId(1);
        userStorage.update(userOne);
        userSecond = userStorage.getUserById(1);
        assertEquals("OneNameUpdate", userSecond.getName(), "Имя не корректно");
        assertEquals("OneLogUpdate", userSecond.getLogin(), "Логин не корректен");
        assertEquals("OneUpdate@mail.ru", userSecond.getEmail(), "Email не корректен");
        assertEquals(LocalDate.of(2000, 2, 2), userSecond.getBirthday(), "Дата рождения не верна");
    }
/*    @Test
    void deleteUser() {
        userOne = User.builder()
                .name("OneName")
                .login("OneLog")
                .email("One@mail.ru")
                .birthday(LocalDate.of(2000,1,1))
                .build();
        userStorage.create(userOne);
        assertEquals("OneName", userOne.getName(), "Имя не корректно");
        userOne.setName("OneNameUpdate");
        userOne.setLogin("OneLogUpdate");
        userOne.setEmail("OneUpdate@mail.ru");
        userOne.setBirthday(LocalDate.of(2000, 2, 2));

        assertEquals("OneNameUpdate", userOne.getName(), "Имя не корректно");
        assertEquals("OneLogUpdate", userOne.getLogin(), "Логин не корректен");
        assertEquals("OneUpdate@mail.ru", userOne.getEmail(), "Email не корректен");
        assertEquals(LocalDate.of(2000, 2, 2), userOne.getBirthday(), "Дата рождения не верна");
    }*/
}