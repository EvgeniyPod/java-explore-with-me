package ru.practicum.place.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto {
    private int id;
    private Float lat;
    private Float lon;
    private Float radius;

    @Length(max = 256, min = 1)
    private String name;
}