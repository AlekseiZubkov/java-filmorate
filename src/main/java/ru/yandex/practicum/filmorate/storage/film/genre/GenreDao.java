package ru.yandex.practicum.filmorate.storage.film.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getGenreAll();

    Genre getGenreById(long id);

    public List<Genre> getGenreFilmById(long id);

    void loadGenres(List<Film> films);
}
