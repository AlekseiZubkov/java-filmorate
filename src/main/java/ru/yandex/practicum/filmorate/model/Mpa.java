package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Mpa {
    private int id;
    private String name;

    public Mpa(int id) {
        this.id = id;
    }
}
