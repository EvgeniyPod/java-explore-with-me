package ru.practicum.eventsinplace;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsInPlaceRepository {
    List<EventsInPlace> findEventsByPlaceId(int placeId, int from, int size);

    List<EventsInPlace> findEventsByPlaceName(String placeName, int from, int size);
}