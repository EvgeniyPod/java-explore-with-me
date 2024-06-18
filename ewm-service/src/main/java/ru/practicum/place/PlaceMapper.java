package ru.practicum.place;

import org.springframework.data.domain.Page;
import ru.practicum.place.dto.NewPlaceDto;
import ru.practicum.place.dto.PlaceDto;

import java.util.List;
import java.util.stream.Collectors;

public class PlaceMapper {

    /**
     * Преобразовать объект NewPlaceDto в объект Place.
     *
     * @param placeDto данные нового места
     * @return объект Place
     */
    public static Place toPlace(NewPlaceDto placeDto) {
        return Place.builder()
                .name(placeDto.getName())
                .lat(placeDto.getLat())
                .lon(placeDto.getLon())
                .radius(placeDto.getRadius())
                .build();
    }

    /**
     * Обновить объект Place на основе данных из PlaceDto.
     *
     * @param place    существующее место
     * @param placeDto новые данные о месте
     * @return обновленный объект Place
     */
    public static Place toPlace(Place place, PlaceDto placeDto) {
        return Place.builder()
                .id(place.getId())
                .name(placeDto.getName() == null ? place.getName() : placeDto.getName())
                .lat(placeDto.getLat() == null ? place.getLat() : placeDto.getLat())
                .lon(placeDto.getLon() == null ? place.getLon() : placeDto.getLon())
                .radius(placeDto.getRadius() == null ? place.getRadius() : placeDto.getRadius())
                .build();
    }

    /**
     * Создать объект Place на основе идентификатора и данных из PlaceDto.
     *
     * @param placeId  идентификатор места
     * @param placeDto данные о месте
     * @return объект Place
     */
    public static Place toPlace(int placeId, PlaceDto placeDto) {
        return Place.builder()
                .id(placeId)
                .name(placeDto.getName())
                .lat(placeDto.getLat())
                .lon(placeDto.getLon())
                .radius(placeDto.getRadius())
                .build();
    }

    /**
     * Преобразовать объект Place в объект PlaceDto.
     *
     * @param place объект места
     * @return объект PlaceDto
     */
    public static PlaceDto toPlaceDto(Place place) {
        return PlaceDto.builder()
                .id(place.getId())
                .name(place.getName())
                .lat(place.getLat())
                .lon(place.getLon())
                .radius(place.getRadius())
                .build();
    }

    /**
     * Преобразовать объект Page<Place> в список объектов PlaceDto.
     *
     * @param place страница с местами
     * @return список объектов PlaceDto
     */
    public static List<PlaceDto> toPlaceDto(Page<Place> place) {
        return place.stream()
                .map(PlaceMapper::toPlaceDto)
                .collect(Collectors.toList());
    }
}
