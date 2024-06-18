package ru.practicum.eventsinplace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.EventState;
import ru.practicum.location.LocationDto;
import ru.practicum.user.dto.UserShortDto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventsInPlace {
    private int id;

    @NotNull
    private String title;

    @NotNull
    private String annotation;

    @NotNull
    private String eventDate;

    @NotNull
    private boolean paid;

    @NotNull
    private CategoryDto category;

    @NotNull
    private UserShortDto initiator;

    @Enumerated(EnumType.STRING)
    private EventState state;

    private String createdOn;

    private String publishedOn;

    private int confirmedRequests;

    private int participantLimit;

    private boolean requestModeration;

    private String description;

    private int views;

    private LocationDto location;
}