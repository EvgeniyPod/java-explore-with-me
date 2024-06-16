package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.EndpointHitStatDto;
import ru.practicum.stats.exception.InvalidPathVariableException;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository endpointHitRepository;

    @Override
    public EndpointHit addHit(EndpointHitDto endpointHitDto) {
        return endpointHitRepository.addHit(endpointHitDto);
    }

    @Override
    public List<EndpointHitStatDto> getStates(String start, String end, String[] uris, boolean unique) {
        if (start.isBlank() || end.isBlank()) {
            throw new InvalidPathVariableException("Некорректные даты: даты не должны быть пустыми");
        }

        Timestamp startDate = Timestamp.valueOf(start);
        Timestamp endDate = Timestamp.valueOf(end);

        if (startDate.after(endDate)) {
            throw new InvalidPathVariableException("Некорректные даты: начальная дата должна быть раньше конечной даты");
        }

        return endpointHitRepository.getHits(start, end, uris, unique);
    }
}
