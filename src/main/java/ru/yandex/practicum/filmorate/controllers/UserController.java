package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        List<User> list = new ArrayList<User>(users.values());
        return list;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validUser(user);
        log.debug("Добавили пользователя: {}", user);
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validUser(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Обновили пользователя: {}", user);
            return user;
        } else {
            throw new ValidationException("Нет такого пользователя в списке");
        }
    }

    private boolean validUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (user.getLogin() == null || user.getEmail().isBlank() || user.getLogin().matches(".*\\s+.*")) {
            throw new ValidationException("Логин   не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть позже ");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return true;
    }
}
