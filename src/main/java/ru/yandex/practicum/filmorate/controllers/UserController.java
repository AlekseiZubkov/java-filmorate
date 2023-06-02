package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")

public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> findAll() {
        log.info("Пришел GET запрос /users");
        log.debug("Текущее количество пользователей: {}", service.findAll().size());
        return service.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Пришел Post запрос /users");
        return service.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        service.updateUser(user);
        log.info("Пришел Put запрос /users");
        return user;
    }

    @GetMapping("/{id}")
    public User findPost(@PathVariable("id") long id) {
        log.info("Пришел Get запрос /users");
        return service.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendToUser(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Пришел Put запрос /users/{}/friends/{}", id, friendId);
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriendToUser(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Пришел Delete запрос /users/{}/friends/{}", id, friendId);
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable long id) {
        log.info("Пришел Get запрос /users/{}/friends", id);
        return service.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getUserCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Пришел Get запрос /users/{}/friends/common/{}", id, otherId);
        return service.getCommonFriends(id, otherId);
    }
}

