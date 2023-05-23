package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validUserTest() {
        User user = getValidUser();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "Валидация некорректна");
    }

    @Test
    void emailUserTest() {
        User user = getValidUser();
        user.setEmail("zmailru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    void emailEmtyUserTest() {
        User user = getValidUser();
        user.setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    void nullLoginTest() {
        User user = getValidUser();
        user.setLogin(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    void whitespaceLoginTest() {
        User user = getValidUser();
        user.setLogin("null rr");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }

    @Test
    void pastBirthdayTest() {
        User user = getValidUser();
        user.setBirthday(LocalDate.of(3000, 8, 11));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Валидация некорректна");
    }
    @Test
    void emptyNameTest() {
        User user = getValidUser();
        user.setName("   ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "Валидация некорректна");
    }
    @Test
    void nullNameTest() {
        User user = getValidUser();
        user.setName(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "Валидация некорректна");
    }
    @Test
    void emptyRequestTest() {
        User user = User.builder()
                .email(null)
                .login(null)
                .name(null)
                .birthday(null)
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.size(), "Валидация некорректна");
    }

    private User getValidUser() {
        return User.builder()
                .email("z@mail.ru")
                .login("Zaleksei")
                .name("Alex")
                .birthday(LocalDate.of(1995, 8, 11))
                .build();
    }
}