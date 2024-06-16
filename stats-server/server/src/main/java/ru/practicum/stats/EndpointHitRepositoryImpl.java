package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.EndpointHitStatDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EndpointHitRepositoryImpl implements EndpointHitRepository {
    private final JdbcTemplate jdbcTemplate;
private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EndpointHit addHit(EndpointHitDto endpointHitDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "insert into endpoint_hit(app, ip, uri, timestamp) values (?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, endpointHitDto.getApp());
            stmt.setString(2, endpointHitDto.getIp());
            stmt.setString(3, endpointHitDto.getUri());
            stmt.setTimestamp(4, Timestamp.valueOf(endpointHitDto.getTimestamp()));
            return stmt;
        }, keyHolder);
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(keyHolder.getKey().intValue(), endpointHitDto);

        log.info("Hit {} добавлен", keyHolder.getKey().intValue());
        return endpointHit;
    }

    @Override
    public List<EndpointHitStatDto> getHits(String start, String end, String[] uris, boolean unique) {
        String cnt_query_str = "count(";
        if (unique) {
            cnt_query_str += "distinct e.ip) hits ";
        } else {
            cnt_query_str += "1) hits ";
        }
        String sql = "select e.app, e.uri, " + cnt_query_str +
                "from endpoint_hit e " +
                "where e.timestamp between ? and ?";
        if (uris != null && uris.length > 0) {
            sql += "and e.uri in (";
            String uriList = "'";
            for (String uri : uris) {
                uriList += uri + "', '";
            }
            sql += uriList.substring(0, uriList.length() - 3) + ") ";
        }
        sql += "group by e.app, e.uri " +
               "order by hits desc";

        log.info("getHits.sql = {}", sql);
return jdbcTemplate.query(sql, this::mapper, LocalDateTime.parse(start, df), LocalDateTime.parse(end, df));
    }

    private EndpointHitStatDto mapper(ResultSet resultSet, int rowNum) throws SQLException {
        return EndpointHitStatDto.builder()
                .app(resultSet.getString("app"))
                .uri(resultSet.getString("uri"))
                .hits(resultSet.getInt("hits"))
                .build();
    }
}