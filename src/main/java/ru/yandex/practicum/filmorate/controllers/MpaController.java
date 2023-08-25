package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> findAllMpa() {
        log.info("Пришел GET запрос /mpa");
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa mpaById(@PathVariable("id") long id) {
        log.info("Пришел GET запрос /mpa/{id}");
        return mpaService.mpaById(id);
    }

}
