package ru.practicum.request;

import ru.practicum.DateFormatter;
import ru.practicum.event.Event;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    /**
     * Преобразует ParticipationRequestDto, пользователя, событие и дату создания в объект Request.
     *
     * @param requestDto данные участия в запросе
     * @param user       пользователь
     * @param event      событие
     * @param created    дата создания запроса
     * @return объект Request
     */
    public static Request toRequest(ParticipationRequestDto requestDto,
                                    User user,
                                    Event event,
                                    LocalDateTime created) {
        return Request.builder()
                .created(created)
                .event(event)
                .user(user)
                .status(requestDto.getStatus())
                .build();
    }

    /**
     * Преобразует список объектов Request в список ParticipationRequestDto.
     *
     * @param requests список объектов Request
     * @return список ParticipationRequestDto
     */
    public static List<ParticipationRequestDto> toRequestDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует объект Request в ParticipationRequestDto для передачи в API.
     *
     * @param request объект Request
     * @return объект ParticipationRequestDto
     */
    public static ParticipationRequestDto toRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(DateFormatter.toString(request.getCreated()))
                .event(request.getEvent().getId())
                .requester(request.getUser().getId())
                .status(request.getStatus())
                .build();
    }


}