package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    void create(Film film);

    void update(Film film);

    void delete(Film film);

    List<Film> getFilms();

    void addLikeByFilm(long id, long userId);

    public List<Film> getPopularFilms(int count);

    Film getFilmById(long id);

    void deleteLikeByFilm(long id, long userId);

    Map<Long, Film> getFilmsMap();
}
