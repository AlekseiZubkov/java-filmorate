package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    @GetMapping
    public List<Film> findAll() {
        log.info("Пришел GET запрос /films");
        log.debug("Текущее количество фильмов: {}", service.findAll().size());
        return service.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Пришел POST запрос /films, с фильмом {}", film);
        service.create(film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Пришел Put запрос /films, с фильмом {}", film);
        service.update(film);
        return film;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info("Пришел Get запрос /films/{}", id);
        return service.getFilmById(id);
    }

    @PutMapping("/{id}/like/{filmId}")
    public void addLikeByFilm(@PathVariable long id, @PathVariable long filmId) {
        log.info("Пришел Put запрос /films/{}/like/{}", id, filmId);
        service.addLikeByFilm(id, filmId);
    }

    @DeleteMapping("/{id}/like/{filmId}")
    public void deleteLikeByFilm(@PathVariable long id, @PathVariable long filmId) {
        log.info("Пришел Delete запрос /films/{}/like/{}", id, filmId);
        service.deleteLikeByFilm(id, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Пришел Get запрос /films/popular");
        return service.getPopularFilms(count);
    }
}
