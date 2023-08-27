package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreStorage;

    public List<Genre> findAll() {
        return genreStorage.getGenreAll();
    }

    public Genre genreById(long id) {
        return genreStorage.getGenreById(id);
    }
}
