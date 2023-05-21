package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<Integer, Film>();
    private int id;
    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        List<Film> list= new ArrayList<Film>(films.values());
        return list;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validFilm(film);
        log.debug("Добавили фильм: {}",film);
        id++;
        film.setId(id);
        films.put(id,film);
        return film;
    }
    @PutMapping
    public Film update(@RequestBody Film film) {
        validFilm(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Обновили фильм: {}",film);
            return film;
        } else {
            throw new ValidationException("Нет такого фильма в списке");
        }
    }
    private boolean validFilm(Film film){
        if(film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым.");
        }
        if(film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть пустым");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Дата релиза не может быть раньше");
        }
        if(film.getDuration() <= 0 ) {
            throw new ValidationException("Продолжительность фильма отрицательная");
        }
        return true;
    }
}
