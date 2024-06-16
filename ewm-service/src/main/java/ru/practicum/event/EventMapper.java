package ru.practicum.event;

import org.springframework.data.domain.Page;
import ru.practicum.DateFormatter;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.location.Location;
import ru.practicum.location.LocationMapper;
import ru.practicum.user.User;
import ru.practicum.user.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * EventMapper отвечает за преобразование между объектами Event и их DTO представлениями.
 */
public class EventMapper {

    /**
     * Преобразует NewEventDto в объект Event.
     *
     * @param eventDto  DTO события для создания
     * @param category  категория события
     * @param location  местоположение события
     * @return объект Event
     */
    public static Event toEvent(NewEventDto eventDto, Category category, Location location) {
        return Event.builder()
                .title(eventDto.getTitle())
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .eventDate(DateFormatter.toLocalDateTime(eventDto.getEventDate()))
                .participantLimit(eventDto.getParticipantLimit() == null ? 0 : eventDto.getParticipantLimit())
                .paid(eventDto.getPaid() != null && eventDto.getPaid())
                .requestModeration(eventDto.getRequestModeration() == null || eventDto.getRequestModeration())
                .category(category)
                .location(location)
                .build();
    }

    /**
     * Преобразует UpdateEventUserRequest в объект Event с учетом существующего события.
     *
     * @param eventDto запрос на обновление от пользователя
     * @param event    существующее событие, которое обновляется
     * @param category категория события
     * @param location местоположение события
     * @param user     пользователь, инициирующий обновление
     * @return объект Event после обновления
     */
    public static Event toEvent(UpdateEventUserRequest eventDto,
                                Event event,
                                Category category,
                                Location location,
                                User user) {
        return Event.builder()
                .id(event.getId())
                .title(eventDto.getTitle() == null ? event.getTitle() : eventDto.getTitle())
                .annotation(eventDto.getAnnotation() == null ? event.getAnnotation() : eventDto.getAnnotation())
                .description(eventDto.getDescription() == null ? event.getDescription() : eventDto.getDescription())
                .eventDate(eventDto.getEventDate() == null ?
                        event.getEventDate() : DateFormatter.toLocalDateTime(eventDto.getEventDate()))
                .participantLimit(eventDto.getParticipantLimit() == null ?
                        event.getParticipantLimit() : eventDto.getParticipantLimit())
                .paid(eventDto.getPaid() == null ? event.isPaid() : eventDto.getPaid())
                .requestModeration(eventDto.getRequestModeration() == null ?
                        event.isRequestModeration() : eventDto.getRequestModeration())
                .category(eventDto.getCategory() == null ? event.getCategory() : category)
                .location(eventDto.getLocation() == null ? event.getLocation() : location)
                .user(event.getUser() == null ? user : event.getUser())
                .state(event.getState())
                .build();
    }

    /**
     * Преобразует UpdateEventAdminRequest в объект Event с учетом существующего события.
     *
     * @param eventDto запрос на обновление от администратора
     * @param event    существующее событие, которое обновляется
     * @param category категория события
     * @param location местоположение события
     * @param user     пользователь, инициирующий обновление
     * @return объект Event после обновления
     */
    public static Event toEvent(UpdateEventAdminRequest eventDto,
                                Event event,
                                Category category,
                                Location location,
                                User user) {
        return Event.builder()
                .id(event.getId())
                .title(eventDto.getTitle() == null ? event.getTitle() : eventDto.getTitle())
                .annotation(eventDto.getAnnotation() == null ? event.getAnnotation() : eventDto.getAnnotation())
                .description(eventDto.getDescription() == null ? event.getDescription() : eventDto.getDescription())
                .eventDate(eventDto.getEventDate() == null ?
                        event.getEventDate() : DateFormatter.toLocalDateTime(eventDto.getEventDate()))
                .participantLimit(eventDto.getParticipantLimit() == null ?
                        event.getParticipantLimit() : eventDto.getParticipantLimit())
                .paid(eventDto.getPaid() == null ? event.isPaid() : eventDto.getPaid())
                .requestModeration(eventDto.getRequestModeration() == null ?
                        event.isRequestModeration() : eventDto.getRequestModeration())
                .category(eventDto.getCategory() == null ? event.getCategory() : category)
                .location(eventDto.getLocation() == null ? event.getLocation() : location)
                .user(event.getUser() == null ? user : event.getUser())
                .state(event.getState())
                .build();
    }

    /**
     * Преобразует объект Event в полный DTO EventFullDto.
     *
     * @param event событие для преобразования
     * @return полный DTO события
     */
    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(DateFormatter.toString(event.getEventDate()))
                .paid(event.isPaid())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .participantLimit(event.getParticipantLimit())
                .createdOn(event.getCreatedOn() == null ? null : DateFormatter.toString(event.getCreatedOn()))
                .publishedOn(event.getPublishedOn() == null ? null : DateFormatter.toString(event.getPublishedOn()))
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .initiator(event.getUser() == null ? null : UserMapper.toUserShortDto(event.getUser()))
                .category(event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()))
                .location(event.getLocation() == null ? null : LocationMapper.toLocationDto(event.getLocation()))
                .build();
    }

    /**
     * Преобразует объект Event в краткий DTO EventShortDto.
     *
     * @param event событие для преобразования
     * @return краткий DTO события
     */
    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(DateFormatter.toString(event.getEventDate()))
                .paid(event.isPaid())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .initiator(event.getUser() == null ? null : UserMapper.toUserShortDto(event.getUser()))
                .category(event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()))
                .build();
    }

    /**
     * Преобразует страницу объектов Event в список кратких DTO EventShortDto.
     *
     * @param events страница событий для преобразования
     * @return список кратких DTO событий
     */
    public static List<EventShortDto> toEventShortDto(Page<Event> events) {
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует страницу объектов Event в список полных DTO EventFullDto.
     *
     * @param events страница событий для преобразования
     * @return список полных DTO событий
     */
    public static List<EventFullDto> toEventFullDto(Page<Event> events) {
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }
}
