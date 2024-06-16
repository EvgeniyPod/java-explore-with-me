package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.RequestService;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    /** Создание события пользователем */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@Positive @PathVariable int userId, @Valid @RequestBody NewEventDto eventDto) {
        log.info("Запрос на создание события {} от пользователя {}", eventDto.getTitle(), userId);
        return eventService.createEvent(userId, eventDto);
    }

    /** Получение списка событий пользователя */
    @GetMapping
    @Transactional(readOnly = true)
    public List<EventShortDto> findByUserId(@Positive @PathVariable int userId,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на получение событий пользователя {} from={} size={}", userId, from, size);
        return eventService.findByUserId(userId, from, size);
    }

    /** Получение запросов для события пользователя */
    @GetMapping("/{eventId}/requests")
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(@Positive @PathVariable int userId,
                                                     @Positive @PathVariable int eventId) {
        log.info("Запрос пользователя {} на получение запросов для события {}", userId, eventId);
        return requestService.findByUserIdAndEventId(userId, eventId);
    }

    /** Обновление события пользователем */
    @PatchMapping("/{eventId}")
    public EventFullDto updateUserEvent(@Positive @PathVariable int userId,
                                        @Positive @PathVariable int eventId,
                                        @Valid @RequestBody UpdateEventUserRequest eventDto) {
        log.info("Запрос на обновление события {} от пользователя {}", eventId, userId);
        return eventService.updateUserEvent(userId, eventId, eventDto);
    }

    /** Обновление статусов запросов для события пользователем */
    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequests(@Positive @PathVariable int userId,
                                                              @Positive @PathVariable int eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest requests) {
        log.info("Запрос на обновление запросов для события {} от пользователя {}", eventId, userId);
        return requestService.updateEventRequests(userId, eventId, requests);
    }

    /** Получение полного описания события пользователя по ID */
    @GetMapping("/{eventId}")
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdAndUserId(@Positive @PathVariable int userId, @Positive @PathVariable int eventId) {
        log.info("Запрос пользователя {} на получение события {}", userId, eventId);
        return eventService.findByIdAndUserId(eventId, userId);
    }


}
