package ru.practicum.stats;

import ru.practicum.dto.EndpointHitDto;

import java.sql.Timestamp;

public class EndpointHitMapper {

    /**
     * Преобразует DTO-объект EndpointHitDto в объект EndpointHit с заданным id.
     *
     * @param id Идентификатор для нового объекта EndpointHit.
     * @param endpointHitDto DTO-объект, который нужно преобразовать.
     * @return Новый объект EndpointHit с заданным id и данными из endpointHitDto.
     */
    public static EndpointHit toEndpointHit(int id, EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .id(id)
                .app(endpointHitDto.getApp())
                .ip(endpointHitDto.getIp())
                .uri(endpointHitDto.getUri())
                .timestamp(Timestamp.valueOf(endpointHitDto.getTimestamp()))
                .build();
    }

    /**
     * Преобразует объект EndpointHit в объект EndpointHitDto.
     *
     * @param endpointHit Объект, который нужно преобразовать.
     * @return DTO-объект с данными из endpointHit или null, если endpointHit равен null.
     */
    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        if (endpointHit == null) {
            return null;
        }
        return EndpointHitDto.builder()
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .timestamp(endpointHit.getTimestamp().toString())
                .build();
    }
}
