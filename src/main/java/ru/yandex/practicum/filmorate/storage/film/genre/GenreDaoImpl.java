package ru.yandex.practicum.filmorate.storage.film.genre;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Primary
@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId((int) rs.getLong("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        };
    }

    @Override
    public List<Genre> getGenreAll() {
        String sql = "SELECT GeNRE_ID, GeNRE_NAME\n" +
                "FROM PUBLIC.GeNRE;\n";

        List<Genre> genreList = jdbcTemplate.query(sql, genreRowMapper());
        return genreList;
    }

    @Override
    public Genre getGenreById(long id) {
        String sql = " SELECT genre_id, genre_NAME FROM genre where genre_id = ?";
        List<Genre> genreList = jdbcTemplate.query(sql, genreRowMapper(), id);
        if (genreList.size() > 0) {
            return genreList.get(0);
        }
        throw new NotFoundException("Жанр не найден");
    }

    @Override
    public void loadGenres(List<Film> films) {

        final Map<Long, Film> mapFilms = films.stream().collect(Collectors.toMap(Film::getId, Function.identity())); // переводим в мапу
        String sizeFilms = String.join(",", Collections.nCopies(films.size(), "?"));// количество  ?
        final String sqlQuery = "SELECT * from genre g, films_genre fg where fg.genre_id = g.genre_id AND fg.film_id in (" + sizeFilms + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            //Получаем из id фильма и извлекли по нему из мапы значение)
            if (!rs.wasNull()) {
                Film film = mapFilms.get(rs.getLong("FILM_ID"));
//                System.out.println("FILM_ID ======= " + rs.getInt("FILM_ID"));
//                System.out.println("ids.get(rs.getInt ======= " + mapFilms.get(rs.getInt("FILM_ID")));
//                System.out.println("rs===== " + rs);
//                System.out.println("ids " + mapFilms);
//                System.out.println("FILM1 ======= " + film);
                if (film != null) {
                    film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name"))); // добав
                    System.out.println("FILM ======= " + film);
                }
                System.out.println("FILM ККККККККККККККК ");
            }
            //Преобразуем коллекцию типа Film к Integer и в массив, так как передавать требуется именно его
        }, films.stream().map(Film::getId).toArray());
    }
}
