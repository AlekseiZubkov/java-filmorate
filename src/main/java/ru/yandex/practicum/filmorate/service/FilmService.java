package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    long id;

    public List<Film> findAll() {
        return filmStorage.getFilms();
    }

    public void create(Film film) {
        ++id;
        film.setId(id);
        filmStorage.create(film);
    }

    public void update(Film film) {
        if (filmStorage.getFilmsMap().containsKey(film.getId())) {
            filmStorage.update(film);
        } else {
            throw new NotFoundException("Нет такого фильма в списке");
        }
    }


    public Film getFilmById(long id) {
        return filmStorage.getFilms().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм № %d не найден", id)));
    }


    public void addLikeByFilm(long id, long userId) {
        if (filmStorage.getFilmsMap().containsKey(id)) {
            filmStorage.getFilmsMap().get(id).getLikes().add(userId);
        } else {
            throw new NotFoundException(String.format("Фильм № %d не найден", id));
        }
    }

    public void deleteLikeByFilm(long id, long userId) {
        if (userId <= 0) {
            throw new NotFoundException("Нет такого пользователя в списке");
        }
        filmStorage.getFilmsMap().get(id).getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }
}
