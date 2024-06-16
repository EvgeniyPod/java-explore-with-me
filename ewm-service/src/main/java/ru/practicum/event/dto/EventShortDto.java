package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private int id;
    private String description;
    private int views;

    @NotNull
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotNull
    private UserShortDto initiator;

    private int confirmedRequests;

    @NotNull
    private String eventDate;

    @NotNull
    private String title;

    @NotNull
    private boolean paid;
}