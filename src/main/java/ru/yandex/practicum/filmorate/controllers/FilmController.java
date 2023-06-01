package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", service.findAll().size());

        return service.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Добавили фильм: {}", film);
        service.create(film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        service.update(film);
            log.debug("Обновили фильм: {}", film);
            return film;
    }
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return service.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeByFilm(@PathVariable   long id ,@PathVariable long userId) {
        service.addLikeByFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeByFilm(@PathVariable long id ,@PathVariable long userId) {
        service.deleteLikeByFilm(id, userId);
    }
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return service.getPopularFilms(count);
    }







    private boolean validFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть пустым");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма отрицательная");
        }
        return true;
    }
}
