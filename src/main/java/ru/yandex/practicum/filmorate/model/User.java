package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private long id;
    @NotBlank(message = "Email пуст")
    @Email(message = "Email не верен")
    private String email;
    @NotBlank(message = "Логин пуст")
    @Pattern(regexp = "\\S+", message = "В логине есть пробелы")
    private String login;
    private String name;
    @Past(message = "День рождения в будущем")
    private LocalDate birthday;
    private Set<Long> friends ;
}
