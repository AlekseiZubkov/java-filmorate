package ru.yandex.practicum.filmorate.storage.film.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {
    List<Mpa> getMpaAll();

    Mpa getmpaById(long id);
}
