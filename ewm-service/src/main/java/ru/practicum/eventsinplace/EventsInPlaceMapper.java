package ru.practicum.eventsinplace;

import ru.practicum.event.dto.EventFullDto;

import java.util.List;
import java.util.stream.Collectors;

public class EventsInPlaceMapper {

    /**
     * Преобразовать объект EventsInPlace в EventFullDto.
     *
     * @param event объект EventsInPlace
     * @return EventFullDto
     */
    public static EventFullDto toEventFullDto(EventsInPlace event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .participantLimit(event.getParticipantLimit())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .initiator(event.getInitiator())
                .category(event.getCategory())
                .location(event.getLocation())
                .build();
    }

    /**
     * Преобразовать список объектов EventsInPlace в список EventFullDto.
     *
     * @param events список объектов EventsInPlace
     * @return список EventFullDto
     */
    public static List<EventFullDto> toEventFullDto(List<EventsInPlace> events) {
        return events.stream()
                .map(EventsInPlaceMapper::toEventFullDto)
                .collect(Collectors.toList());
    }
}
