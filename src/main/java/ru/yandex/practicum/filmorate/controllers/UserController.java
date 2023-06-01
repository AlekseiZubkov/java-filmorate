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
       log.debug("Текущее количество пользователей: {}", service.findAll().size());
        return service.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return service.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
            service.updateUser(user);
            log.debug("Обновили пользователя: {}", user);
            return user;
    }
    @GetMapping("{id}")
    public User findPost(@PathVariable("id") long id) {
        log.debug("щем пользователя с ID: {}", id);
        return service.getUserById(id);
}
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendToUser(@PathVariable("id") long id, @PathVariable("friendId")long friendId) {
        log.debug("id={},friendId={}",id,friendId);
        service.addFriend(id, friendId);
    }
}

