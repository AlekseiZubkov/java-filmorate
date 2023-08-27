package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreDao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

@Primary
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private final GenreDao genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmStorage filmStorage, GenreDao genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public void create(Film film) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = Map.of("name", film.getName(),
                "description", film.getDescription(), "releasedate", film.getReleaseDate().toString(),
                "duration", film.getDuration(), "mpa_id", film.getMpa().getId()
        );
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId((Long) id);
        if (film.getGenres().size() != 0) {
            for (Genre genres : film.getGenres()) {
                String sql = "INSERT INTO FILMs_GENRE (FILM_ID, GENRE_ID) VALUES (?,?);";
                jdbcTemplate.update(sql, film.getId(), genres.getId());
            }
        }
    }

    @Override
    public void update(Film film) {
        if (checkId(film.getId())) {
            String sql = "UPDATE films SET name = ?, description = ?, duration = ?, releaseDate = ?, mpa_id = ? " +
                    " WHERE id = ? ";
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getDuration(),
                    Date.valueOf(film.getReleaseDate()), film.getMpa().getId(), film.getId()
            );
        } else {
            throw new NotFoundException("Нет такого фильма в списке");
        }
        if (film.getGenres().size() > 0) {
            List<Genre> currentGenres = genreStorage.getGenreFilmById(film.getId()); // Получаем список жанров,
            // присутствующих в базе данных
            List<Genre> genresToRemove = new ArrayList<>(currentGenres);
            genresToRemove.removeAll(film.getGenres());
            for (Genre genre : genresToRemove) {
                jdbcTemplate.update("DELETE FROM films_genre WHERE FILM_ID = ? AND GENRE_ID = ?;",
                        film.getId(), genre.getId());
            }
            List<Genre> newGenres = new ArrayList<>(new HashSet<>(film.getGenres()));
            newGenres.removeAll(currentGenres);
            Collections.reverse(newGenres);
            film.setGenres(newGenres);
            for (Genre genre : newGenres) {
                jdbcTemplate.update("INSERT INTO films_genre (FILM_ID, GENRE_ID) VALUES (?,?);", film.getId(),
                        genre.getId());
            }
        } else {
            jdbcTemplate.update("DELETE FROM films_genre WHERE FILM_ID = ?;", film.getId());
        }

    }


    @Override
    public void delete(Film film) {
        String sql = "DELETE FROM films " +
                "WHERE id = ?;";        // текст SQL запроса
        jdbcTemplate.update(sql, film.getId());
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT DISTINCT f.*, mp.mpa_id, mp.mpa_name,\n" +
                "                ge.genre_id, ge.genre_name,fg.film_id \n" +
                "FROM FILMS AS f\n" +
                "LEFT JOIN mpa mp ON f.mpa_id = mp.mpa_id\n" +
                "LEFT JOIN films_genre fg ON f.id = fg.film_id\n" +
                "LEFT JOIN genre ge ON fg.genre_id = ge.genre_id\n" +
                "LEFT JOIN likes li ON f.id = li.film_id\n" +
                "GROUP BY f.id,ge.genre_id;";

        List<Film> list = jdbcTemplate.query(sql, filmsRowMapper());

        return list;

    }

    @Override
    public void addLikeByFilm(long id, long userId) {
        if (checkId(id)) {
            String sql = "INSERT INTO LIKES " +
                    "(USER_ID, FILM_ID) " +
                    "VALUES(?, ?);";
            jdbcTemplate.update(sql, userId, id);
        } else {
            throw new NotFoundException(String.format("Фильм № %d не найден", id));
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT f.id, f.name,f.description,f.releaseDate, duration,mp.mpa_id , mp.mpa_name,ge.genre_id, ge.genre_name,fg.film_id " +
                "FROM FILMS f \n" +
                "LEFT JOIN LIKES l ON f.ID = l.FILM_ID \n" +
                "LEFT JOIN mpa mp ON f.mpa_id = mp.mpa_id\n" +
                "LEFT JOIN films_genre fg ON f.id = fg.film_id\n" +
                "LEFT JOIN genre ge ON fg.genre_id = ge.genre_id\n" +
                "GROUP BY F.ID ,ge.genre_id \n" +
                "ORDER BY count(DISTINCT l.USER_ID) DESC, F.ID \n" +
                "LIMIT ?";

        List<Film> list = jdbcTemplate.query(sql, filmsRowMapper(), count);
        return list;
    }

    @Override
    public Film getFilmById(long id) {
        if (checkId(id)) {
            String sql = "SELECT  f.*, mp.mpa_id, mp.mpa_name,\n" +
                    "                ge.genre_id, ge.genre_name,fg.film_id \n" +
                    "FROM FILMS AS f\n" +
                    "LEFT JOIN mpa mp ON f.mpa_id = mp.mpa_id\n" +
                    "LEFT JOIN films_genre fg ON f.id = fg.film_id\n" +
                    "LEFT JOIN genre ge ON fg.genre_id = ge.genre_id\n" +
                    "LEFT JOIN likes li ON f.id = li.film_id\n" +
                    "where f.id = ? " +
                    "GROUP BY f.id,ge.genre_id; ";
            List<Film> list = jdbcTemplate.query(sql, filmsRowMapper(), id);
            return list.get(0);
        } else {
            throw new NotFoundException(String.format("Фильм № %d не найден", id));
        }
    }

    @Override
    public void deleteLikeByFilm(long id, long userId) {
        if (checkId(id)) {
            String sql = "DELETE FROM LIKES " +
                    "WHERE USER_ID=? AND FILM_ID=?;";
            jdbcTemplate.update(sql, userId, id);
        } else {
            throw new NotFoundException(String.format("Фильм № %d не найден", id));
        }
    }

    @Override
    public Map<Long, Film> getFilmsMap() {
        return null;
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));

            return film;
        };
    }

    private RowMapper<Film> filmListRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            long filmid = rs.getLong("id");
            film.setId(rs.getLong("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            List<Genre> genres = new ArrayList<>();

            do {
                int ganreId = rs.getInt("genre_id");
                if (ganreId != 0) {
                    Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
                    genres.add(genre);
                }

            } while (rs.next());

            film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
            return film;
        };
    }

    public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkId(long id) {       //Проверка на присутствие фильма с нужным id
        String sql = "SELECT COUNT(*) FROM films WHERE id = ? ;";
        int filmCount = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return filmCount > 0;
    }

    private RowMapper<Film> filmsRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            Mpa mpa = new Mpa(rs.getInt("mpa_id"));
            mpa.setName(rs.getString("mpa_name"));
            film.setMpa(mpa);
            return film;
        };
    }
}
