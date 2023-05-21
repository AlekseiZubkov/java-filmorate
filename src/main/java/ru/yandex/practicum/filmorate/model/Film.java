package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Data
public class Film {
    private int id;
    @NotNull
    @NotBlank
    @Email
    private  String name;
    private  String description;
    private LocalDate releaseDate;
    private int duration;
}
