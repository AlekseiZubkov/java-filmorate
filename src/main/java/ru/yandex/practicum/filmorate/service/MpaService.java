package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDao mpaStorage;

    public List<Mpa> findAll() {
        return mpaStorage.getMpaAll();
    }
    public Mpa mpaById(long id) {
        return mpaStorage.getmpaById(id);
    }
}
