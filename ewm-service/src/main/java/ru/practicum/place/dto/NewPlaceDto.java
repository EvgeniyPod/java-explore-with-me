package ru.practicum.place.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewPlaceDto {
    @NotNull
    @Length(max = 256, min = 1)
    private String name;

    @NotNull
    private Float lat;

    @NotNull
    private Float lon;

    @NotNull
    private Float radius;
}