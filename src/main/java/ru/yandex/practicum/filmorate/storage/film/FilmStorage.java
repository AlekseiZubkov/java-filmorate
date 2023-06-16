package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    void create(Film film);

    void update(Film film);

    void delete(Film film);

    List<Film> getFilms();


    Film getFilmById(long id);

    Map<Long, Film> getFilmsMap();
}
