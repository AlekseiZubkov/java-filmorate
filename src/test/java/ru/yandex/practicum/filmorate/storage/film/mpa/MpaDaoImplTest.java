package ru.yandex.practicum.filmorate.storage.film.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDaoImplTest {
    private final MpaDao mpaStorage;
    private final JdbcTemplate jdbcTemplate;


    @BeforeEach
    void beforeEach() {

    }

    @AfterEach
    void afterEach() {
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
    void getMpaAll() {
        List<Mpa> mpaList = mpaStorage.getMpaAll();
        assertEquals(5, mpaList.size(), "Продолжительность неверна");
        assertEquals("PG-13", mpaList.get(2).getName(), "Имя неверно");
    }

    @Test
    void getmpaById() {
        Mpa getMpa = mpaStorage.getmpaById(2);
        assertEquals("PG", getMpa.getName(), "Имя неверно");
    }
}