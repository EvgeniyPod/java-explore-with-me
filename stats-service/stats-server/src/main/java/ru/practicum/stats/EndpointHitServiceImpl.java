package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final EndpointHitRepository endpointHitRepository;

    /**
     * Получает статистику по запросам за указанный период времени и с указанными URI.
     * Если параметр unique установлен в true, возвращает уникальные по IP адресам запросы.
     * Вызывает исключение InvalidPathVariableException в случае некорректных дат.
     */
    @Override
    public List<EndpointHitStatDto> getStates(String start, String end, String[] uris, boolean unique) {
        // Проверка на пустые даты
        if (start.isBlank() || end.isBlank()) {
            throw new InvalidPathVariableException("Некорректные даты: даты не должны быть пустыми");
        }

        // Проверка, что дата начала раньше или равна дате окончания
        Timestamp startDate = Timestamp.valueOf(start);
        Timestamp endDate = Timestamp.valueOf(end);
        if (startDate.after(endDate)) {
            throw new InvalidPathVariableException("Некорректные даты: начальная дата должна быть раньше или равна конечной дате");
        }

        // Получение статистики из репозитория
        return endpointHitRepository.getHits(start, end, uris, unique);
    }

    /**
     * Добавляет запись о запросе.
     */
    @Override
    public EndpointHit addHit(EndpointHitDto endpointHitDto) {
        return endpointHitRepository.addHit(endpointHitDto);
    }
}
