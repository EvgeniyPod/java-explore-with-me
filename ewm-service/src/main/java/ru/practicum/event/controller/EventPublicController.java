package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.practicum.DateFormatter;
import ru.practicum.client.ClientStatsPost;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {
    private static final String APP_NAME = "explore-with-me";
    private final EventService eventService;
    private final ClientStatsPost clientStats;

    /** Получение полной информации о событии по ID */
    @GetMapping("/{id}")
    public EventFullDto getEventByIdAndState(@Positive @PathVariable int id, HttpServletRequest request) {
        log.info("Запрос на получение события {}", id);

        EventFullDto event = eventService.findByIdAndState(id);
        event.setViews(event.getViews() + 1);

        log.info("IP клиента: {}", request.getRemoteAddr());
        log.info("Путь эндпоинта: {}", request.getRequestURI());
        clientStats.addStatInfo(EndpointHitDto.builder()
                .app(APP_NAME)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(DateFormatter.toString(LocalDateTime.now()))
                .build());

        return event;
    }

    /** Получение списка публичных событий */
    @GetMapping
    public List<EventShortDto> findPublicEvents(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) Integer[] categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false) String rangeStart,
                                                @RequestParam(required = false) String rangeEnd,
                                                @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "10") int size,
                                                HttpServletRequest request) {
        log.info("Запрос на получение списка событий text={} categories={} paid={} rangeStart={} rangeEnd={} onlyAvailable={} " +
                        "sort={} from={} size={}", text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);

        List<EventShortDto> events = eventService.findPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        log.info("IP клиента: {}", request.getRemoteAddr());
        log.info("Путь эндпоинта: {}", request.getRequestURI());
        clientStats.addStatInfo(EndpointHitDto.builder()
                .app(APP_NAME)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(DateFormatter.toString(LocalDateTime.now()))
                .build());

        return events;
    }
}
