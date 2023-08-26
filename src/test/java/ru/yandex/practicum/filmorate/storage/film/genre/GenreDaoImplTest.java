package ru.yandex.practicum.filmorate.storage.film.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDaoImplTest {
    private final GenreDao genreStorage;
    private final JdbcTemplate jdbcTemplate;



    @Test
    void getGenreAll() {
        List<Genre> mpaList = genreStorage.getGenreAll();
        assertEquals(6, mpaList.size(), "Кол-во жанров неверно ");
        assertEquals("Мультфильм", mpaList.get(2).getName(), "Имя неверно");
    }


    @Test
    void getGenreById() {
        Genre getGenre = genreStorage.getGenreById(5);
        assertEquals("Документальный", getGenre.getName(), "Имя неверно");
        getGenre = genreStorage.getGenreById(6);
        assertEquals("Боевик", getGenre.getName(), "Имя неверно");
    }
}