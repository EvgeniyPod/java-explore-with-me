package ru.practicum.place;

import ru.practicum.place.dto.NewPlaceDto;
import ru.practicum.place.dto.PlaceDto;

import java.util.List;

public interface PlaceService {
    PlaceDto createPlace(NewPlaceDto placeDto);

    PlaceDto updatePlace(int placeId, PlaceDto placeDto);

    void deletePlace(int placeId);

    List<PlaceDto> getPlaces(int from, int size);

    PlaceDto getPlaceById(int placeId);
}