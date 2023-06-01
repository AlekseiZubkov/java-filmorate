package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.HashSet;
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
    public Film create(Film film){
        if (film.getLikes() == null) {        //если списка нет, то присвоить пустой список
            film.setLikes(new HashSet<>());
        }
        ++id;
        film.setId(id);
        filmStorage.create(film);
        return film;
    }
    public Film update(Film film) {
        if (filmStorage.getFilmsMap().containsKey(film.getId())) {
            filmStorage.getFilmsMap().put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Нет такого фильма в списке");
        }
    }
    public void deleteUser(Film film) {
        filmStorage.delete(film);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilms().stream()
                .filter(u -> u.getId()==id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Фильм № %d не найден", id)));
    }


    public void addLikeByFilm(long id, long userId) {
        if (checkInList(id) && checkInList(id)){
            filmStorage.getFilmsMap().get(id).getLikes().add(userId);
        }
    }
    public void deleteLikeByFilm(long id, long userId) {
            filmStorage.getFilmsMap().remove(id);
    }
    public List<Film> getPopularFilms(int count) {
                 return filmStorage.getFilms().stream()
                         .sorted((f1,f2)->f2.getLikes().size() - f1.getLikes().size())
                         .limit(count).collect(Collectors.toList());
    }

    private boolean checkInList (long id){      //Проверка, что пользователь с таким ID есть в списке
        if (filmStorage.getFilmsMap().containsKey(id)) {
            return true;
        } else {
            throw new UserNotFoundException(String.format("Фильм № %d не найден", id));
        }
    }

}
