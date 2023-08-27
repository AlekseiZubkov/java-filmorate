package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
            private final FilmStorage filmStorage;
            private final GenreDao genreStorage;

    public List<Film> findAll() {
        List<Film> films = filmStorage.getFilms();
        genreStorage.loadGenres(films);
        return films;
    }

    public void create(Film film) {
        filmStorage.create(film);
    }

    public void update(Film film) {
            filmStorage.update(film);
    }


    public Film getFilmById(long id) {

        Film film1 = filmStorage.getFilmById(id);
         genreStorage.loadGenres(List.of(film1));

        return film1;

    }


    public void addLikeByFilm(long id, long userId) {
        filmStorage.addLikeByFilm(id,userId);
    }

    public void deleteLikeByFilm(long id, long userId) {
        if (id <= 0 || userId <= 0) {
            throw new NotFoundException("Не корректный ID запроса");
        }
        filmStorage.deleteLikeByFilm(id,userId);

    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
