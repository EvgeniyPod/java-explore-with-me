package ru.practicum.stats;

import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.EndpointHitStatDto;

import java.util.List;

@Service
public interface EndpointHitService {
    EndpointHit addHit(EndpointHitDto endpointHitDto);

    List<EndpointHitStatDto> getStates(String start, String end, String[] uris, boolean unique);
}