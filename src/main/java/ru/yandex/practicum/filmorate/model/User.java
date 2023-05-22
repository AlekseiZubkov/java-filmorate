package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @NotBlank(message = "Email пуст")
    @Email(message = "Email не верен")
    private String email;
    @NotBlank(message = "Логин пуст")
    @Pattern(regexp = "\\S+", message = "В логине есть пробелы")
    private String login;

    private String name;
    @Past(message = "День рождения в будущем")
    private LocalDate birthday;

}
