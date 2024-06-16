package ru.practicum.location;

public class LocationMapper {

    /**
     * Преобразует объект LocationDto в объект Location.
     *
     * @param locationDto объект LocationDto, который нужно преобразовать
     * @return объект Location или null, если входной параметр null
     */
    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lon(locationDto.getLon())
                .lat(locationDto.getLat())
                .build();
    }

    /**
     * Преобразует объект Location в объект LocationDto.
     *
     * @param location объект Location, который нужно преобразовать
     * @return объект LocationDto или null, если входной параметр null
     */
    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }
}