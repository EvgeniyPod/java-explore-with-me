package ru.practicum.stats;

import org.springframework.stereotype.Repository;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.EndpointHitStatDto;

import java.util.List;

@Repository
public interface EndpointHitRepository {
    EndpointHit addHit(EndpointHitDto endpointHitDto);

    List<EndpointHitStatDto> getHits(String start, String end, String[] uris, boolean unique);
}