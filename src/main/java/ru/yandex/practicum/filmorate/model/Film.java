package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank(message = "Имя не задано")
    private String name;
    @Size(max = 200, message = "Описание слишком длинное")
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
}
