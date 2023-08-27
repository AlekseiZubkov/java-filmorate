package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Film {
    private long id;
    @NotBlank(message = "Имя не задано")
    private String name;
    @Size(max = 200, message = "Описание слишком длинное")
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    @Builder.Default
    private Set<Long> likes = new HashSet<>();
    private Mpa mpa;
    @Builder.Default
    private List<Genre> genres = new ArrayList<>();

}
