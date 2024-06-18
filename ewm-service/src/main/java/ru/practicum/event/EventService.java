package ru.practicum.event;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;

import java.util.List;

public interface EventService {
    EventFullDto createEvent(int userId, NewEventDto eventDto);

    List<EventShortDto> findByUserId(int userId, int from, int size);

    EventFullDto findByIdAndUserId(int eventId, int userId);

    EventFullDto updateUserEvent(int userId, int eventId, UpdateEventUserRequest eventDto);

    EventFullDto findByIdAndState(int eventId);

    Event getEventById(int eventId);

    EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest eventDto);

    List<EventShortDto>  findPublicEvents(String text,
                                          Integer[] categories,
                                          Boolean paid,
                                          String rangeStart,
                                          String rangeEnd,
                                          boolean onlyAvailable,
                                          String sort,
                                          int from,
                                          int size);



    List<EventFullDto> findAdminEvents(Integer[] users,
                                       String[] states,
                                       Integer[] categories,
                                       String rangeStart,
                                       String rangeEnd,
                                       int from,
                                       int size);

    List<EventFullDto> findEventsByPlaceId(int placeId, int from, int size);

    List<EventFullDto> findEventsByPlaceName(String placeName, int from, int size);

}