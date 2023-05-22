package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Имя не задано")
    private String name;
    @Size(max = 200,message = "Описание слишком длинное")
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
}
