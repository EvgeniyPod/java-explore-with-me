package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.EndpointHitStatDto;
import ru.practicum.stats.exception.InvalidPathVariableException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EndpointHitController {

    private final EndpointHitService endpointHitService;

    /**
     * Обработчик GET запроса для получения статистики по хитам за указанный период.
     * Бросает исключение, если параметры start или end равны null.
     */
    @GetMapping("/stats")
    public List<EndpointHitStatDto> getStates(@RequestParam(name = "start", required = true) String start,
                                              @RequestParam(name = "end", required = true) String end,
                                              @RequestParam(name = "uris", required = false) String[] uris,
                                              @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        if (start == null || end == null) {
            throw new InvalidPathVariableException("Параметры даты не должны быть null");
        }
        if (unique) {
            log.info("Получение статистики с {} по {} с фильтром по uris={} для уникальных IP", start, end, uris);
        } else {
            log.info("Получение уникальной статистики с {} по {} с фильтром по uris={}", start, end, uris);
        }
        return endpointHitService.getStates(start, end, uris, unique);
    }

    /**
     * Обработчик POST запроса для добавления нового хита.
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit addHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Запрос на добавление хита {}", endpointHitDto.getUri());
        return endpointHitService.addHit(endpointHitDto);
    }
}
