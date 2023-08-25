package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Primary
@Component
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Mpa> getMpaAll() {
        String sql = " SELECT MPA_ID, MPA_NAME FROM PUBLIC.MPA;";
        return jdbcTemplate.query(sql, mpaRowMapper());
    }

    @Override
    public Mpa getmpaById(long id) throws NotFoundException {
        String sql = " SELECT MPA_ID, MPA_NAME FROM PUBLIC.MPA where mpa_id = ?";
        List<Mpa> mpaList = jdbcTemplate.query(sql, mpaRowMapper(), id);
        if (mpaList.size() > 0) {
            return mpaList.get(0);
        }
        throw new NotFoundException("MPA  не найден");
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId((int) rs.getLong("mpa_id"));
            mpa.setName(rs.getString("mpa_name"));
            return mpa;
        };
    }
}
