package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.location.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotNull
    @Length(max = 120, min = 3)
    private String title;

    @NotNull
    @Length(max = 2000, min = 20)
    private String annotation;

    @NotBlank
    @Length(max = 7000, min = 20)
    private String description;

    @NotNull
    private int category;

    @NotNull
    private String eventDate;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private LocationDto location;
}