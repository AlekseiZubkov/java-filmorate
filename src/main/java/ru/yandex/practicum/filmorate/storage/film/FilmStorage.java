package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    void add (Film film);

    void update (Film film);

    void delete (Film film);
    List<Film> getFilmsList();

    Film getFilmById(long id);
}
