package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    private final EventService eventService;

    /** Получение списка событий администратором */
    @GetMapping
    @Transactional(readOnly = true)
    public List<EventFullDto> findAdminEvents(@RequestParam(required = false) Integer[] users,
                                              @RequestParam(required = false) String[] states,
                                              @RequestParam(required = false) Integer[] categories,
                                              @RequestParam(required = false) String rangeStart,
                                              @RequestParam(required = false) String rangeEnd,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на получение событий пользователей={} категории={} состояния={} rangeStart={}" +
                        " rangeEnd={} from={} size={}",
                users, categories, states, rangeStart, rangeEnd, from, size);
        return eventService.findAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /** Обновление события администратором по его ID */
    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@Positive @PathVariable int eventId,
                                           @Valid @RequestBody UpdateEventAdminRequest eventDto) {
        log.info("Запрос на обновление события {} ", eventId);
        return eventService.updateEventByAdmin(eventId, eventDto);
    }

    /** Возвращает список событий по имени места. */
    @GetMapping("/places")
    @Transactional(readOnly = true)
    public List<EventFullDto> findEventsByPlaceName(@RequestParam String placeName,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на получение событий в месте с именем {} from={} size={}", placeName, from, size);
        return eventService.findEventsByPlaceName(placeName, from, size);
    }

    /** Возвращает список событий по идентификатору места. */
    @GetMapping("/places/{placeId}")
    @Transactional(readOnly = true)
    public List<EventFullDto> findEventsByPlaceId(@Positive @PathVariable int placeId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                  @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на получение событий в месте с id {} from={} size={}", placeId, from, size);
        return eventService.findEventsByPlaceId(placeId, from, size);
    }
}
