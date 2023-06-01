package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    public void add(Film film) {
        film.setId(id++);
        films.put(id,film);
    }


    public void update(Film film) {

    }


    public void delete(Film film) {

    }


    public List<Film> getFilmsList() {
        return null;
    }


    public Film getFilmById(long id) {
        return null;
    }

}
