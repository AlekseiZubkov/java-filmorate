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
    public List<Genre> getGenreFilmById(long id) {

        String sql = "SELECT ge.genre_id, ge.genre_NAME FROM films_genre fg " +
                "LEFT JOIN genre ge ON fg.genre_id = ge.genre_id " +
                "WHERE film_id = ?";

        List<Genre> genreList = jdbcTemplate.query(sql, genreRowMapper(), id);
        return genreList;
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
                if (film != null) {
                    film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
                }
            }

        }, films.stream().map(Film::getId).toArray());
    }
}
