package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private  final JdbcTemplate jdbcTemplate;
    private final Map<Long, Film> films = new HashMap<>();

    public InMemoryFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Film film) {
        films.put(film.getId(), film);
    }


    public void update(Film film) {
        films.put(film.getId(), film);
    }


    public void delete(Film film) {
        films.remove(film.getId());
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLikeByFilm(long id, long userId) {

    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }

    public Map<Long, Film> getFilmsMap() {
        return films;
    }

    public Film getFilmById(long id) {
        return films.get(id);
    }

    @Override
    public void deleteLikeByFilm(long id, long userId) {

    }
}

