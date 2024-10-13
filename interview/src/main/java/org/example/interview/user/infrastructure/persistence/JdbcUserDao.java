package org.example.interview.user.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.example.interview.user.application.model.UserSearchRequest;
import org.example.interview.user.infrastructure.data.UserDto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcUserDao implements UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<UserDto> search(UserSearchRequest request) {
        return jdbcTemplate.query(
                UserSql.INSTANCE.search(request),
                generateSqlParams(request),
                BeanPropertyRowMapper.newInstance(UserDto.class)
        );
    }

    @Override
    public int searchCount(UserSearchRequest request) {
        var result = jdbcTemplate.queryForObject(
                UserSql.INSTANCE.searchCount(request),
                generateSqlParams(request),
                Integer.class);
        return result == null ? 0 : result;
    }

    private MapSqlParameterSource generateSqlParams(UserSearchRequest request) {
        int offset = (request.getPage() - 1) * request.getLimit();
        return new MapSqlParameterSource()
                .addValue("ids", request.getIds())
                .addValue("email", request.getEmail())
                .addValue("phone", request.getPhone())
                .addValue("fullName", request.getFullName())
                .addValue("firstName", request.getFirstName())
                .addValue("lastName", request.getLastName())
                .addValue("national", request.getNational())
                .addValue("role", request.getRole())
                .addValue("type", request.getType())
                .addValue("language", request.getLanguage())
                .addValue("createdAtMin", bindParam(request.getCreatedAtMin()))
                .addValue("createdAtMax", bindParam(request.getCreatedAtMax()))
                .addValue("activatedAtMin", bindParam(request.getActivatedAtMin()))
                .addValue("activatedAtMax", bindParam(request.getActivatedAtMax()))
                .addValue("query", "%" + request.getQuery() + "%")
                .addValue("offset", offset)
                .addValue("limit", request.getLimit());
    }

    public static Timestamp bindParam(Instant param) {
        if (param == null) return null;
        return Timestamp.from(param);
    }
}
