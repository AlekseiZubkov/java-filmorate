package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validFilmTest() {
        Film film = getValidFilm();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "Валидация некорректна");
    }

    @Test
    void emptyNameFilmTest() {
        Film film = getValidFilm();
        film.setName("  ");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    void nullNameFilmTest() {
        Film film = getValidFilm();
        film.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    void longDescriptionFilmTest() {
        Film film = getValidFilm();
        film.setDescription("Жизнь десятилетнего Гарри Поттера нельзя назвать сладкой: родители умерли, " +
                "едва ему исполнился год, " +
                "а от дяди и тёти, взявших сироту на воспитание," +
                " достаются лишь тычки да подзатыльники. Но в одиннадцатый день рождения Гарри всё меняется." +
                " Странный гость, неожиданно появившийся на пороге, приносит письмо," +
                " из которого мальчик узнаёт," +
                " что на самом деле он - волшебник и зачислен в школу магии под названием Хогвартс. ");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    void minReleaseDateFilmTest() {
        Film film = getValidFilm();
        film.setReleaseDate(LocalDate.of(1000, 10, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    void negativeDurationTest() {
        Film film = getValidFilm();
        film.setDuration(-10);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    void zeroDurationTest() {
        Film film = getValidFilm();
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    void nullFilmTest() {
        Film film = Film.builder()
                .name(null)
                .description(null)
                .releaseDate(null)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(2, violations.size(), "Валидация некорректна");
    }

    private Film getValidFilm() {
        return Film.builder()
                .name("Гарри Поттер и философский камень")
                .description("Фильм про мальчика который выжил...")
                .releaseDate(LocalDate.of(2001, 11, 4))
                .duration(152)
                .build();
    }
}