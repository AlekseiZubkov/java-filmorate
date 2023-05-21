package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;
  /* @NotNull(message = "Email null")
    @NotBlank(message = "Email пуст")
    @Email(regexp = ".+[@]",message = "Email не верен")*/

    private  String email;
  /*  @NotNull(message = "Логин null")
    @NotBlank(message = "Логин ПУСТ")
    @Pattern(regexp = " ",message = "В логине пробелы")*/
    private  String login;

    private  String name;
  private LocalDate birthday;

}
