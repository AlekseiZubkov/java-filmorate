package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {


    private final FilmStorage filmStorage;
    private Film film1;
    private Film film2;
    private final JdbcTemplate jdbcTemplate;

    private Mpa mpa1 = new Mpa(1, "G");

    @BeforeEach
    void beforeEach() {
        film1 = Film.builder()
                .name("1name")
                .description("1description")
                .duration(100)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpa(mpa1)
                .build();
        film2 = Film.builder()
                .name("2name")
                .description("2description")
                .duration(200)
                .mpa(mpa1)
                .releaseDate(LocalDate.of(2002, 2, 2))
                .build();

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
    void create() {
        filmStorage.create(film1);
        assertEquals("1name", film1.getName(), "Имя некорректно");
        assertEquals("1description", film1.getDescription(), "Описание некорректно");
        assertEquals(100, film1.getDuration(), "Продолжительность неверна");
        assertEquals(LocalDate.of(2000, 1, 1), film1.getReleaseDate(), "Дата выхода неверна");
    }


    @Test
    void update() {
        filmStorage.create(film1);
        film1.setName("1nameUp");
        film1.setDescription("1descriptionUp");
        film1.setDuration(101);
        filmStorage.update(film1);
        assertEquals("1nameUp", film1.getName(), "Имя некорректно");
        assertEquals("1descriptionUp", film1.getDescription(), "Описание некорректно");
        assertEquals(101, film1.getDuration(), "Продолжительность неверна");
        assertEquals(LocalDate.of(2000, 1, 1), film1.getReleaseDate(), "Дата выхода неверна");
    }

    @Test
    void delete() {
        filmStorage.create(film1);
        List<Film> listFilm = filmStorage.getFilms();
        assertFalse(listFilm.isEmpty(), "Неверное кол-во пользователей");
        film1.setId(listFilm.get(0).getId());
        filmStorage.delete(film1);
        listFilm = filmStorage.getFilms();
        assertEquals(0, listFilm.size(), "Продолжительность неверна");
    }

    @Test
    void getFilms() {
        filmStorage.create(film1);
        filmStorage.create(film2);
        List<Film> listFilm = filmStorage.getFilms();

        assertEquals("1name", listFilm.get(0).getName(), "Имя некорректно");
        assertEquals("1description", listFilm.get(0).getDescription(), "Описание некорректно");
        assertEquals(100, listFilm.get(0).getDuration(), "Продолжительность неверна");
        assertEquals(LocalDate.of(2000, 1, 1), listFilm.get(0).getReleaseDate(), "Дата выхода неверна");

        assertEquals("2name", listFilm.get(1).getName(), "Имя некорректно");
        assertEquals("2description", listFilm.get(1).getDescription(), "Описание некорректно");
        assertEquals(200, listFilm.get(1).getDuration(), "Продолжительность неверна");
        assertEquals(LocalDate.of(2002, 2, 2), listFilm.get(1).getReleaseDate(), "Дата выхода неверна");
    }

    @Test
    void getFilmById() {
        filmStorage.create(film1);
        filmStorage.create(film2);
        List<Film> listFilm = filmStorage.getFilms();
        Film filmById = filmStorage.getFilmById(film2.getId());
        assertEquals("2name", filmById.getName(), "Имя некорректно");
        assertEquals("2description", filmById.getDescription(), "Описание некорректно");
        assertEquals(200, filmById.getDuration(), "Продолжительность неверна");
        assertEquals(LocalDate.of(2002, 2, 2), filmById.getReleaseDate(), "Дата выхода неверна");
    }

}
