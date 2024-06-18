package ru.practicum.place.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.place.PlaceService;
import ru.practicum.place.dto.NewPlaceDto;
import ru.practicum.place.dto.PlaceDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Transactional
@RequestMapping(path = "/admin/places")
public class PlaceAdminController {
    private final PlaceService placeService;

    /** Создание нового места. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaceDto createPlace(@Valid @RequestBody NewPlaceDto placeDto) {
        log.info("Запрос на создание места {}", placeDto.getName());
        return placeService.createPlace(placeDto);
    }

    /** Обновить информацию о месте по его идентификатору. */
    @PatchMapping("/{placeId}")
    public PlaceDto updatePlace(@Positive @PathVariable int placeId, @Valid @RequestBody PlaceDto placeDto) {
        log.info("Запрос на обновление места {}", placeId);
        return placeService.updatePlace(placeId, placeDto);
    }

    /** Получить список мест */
    @GetMapping
    @Transactional(readOnly = true)
    public List<PlaceDto> getPlaces(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на получение списка мест from={} size={}", from, size);
        return placeService.getPlaces(from, size);
    }

    /** Удалить место по его идентификатору. */
    @DeleteMapping("/{placeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlace(@Positive @PathVariable int placeId) {
        log.info("Удаление места по идентификатору {} ", placeId);
        placeService.deletePlace(placeId);
    }

    /** Получить информацию о месте по его идентификатору. */
    @GetMapping("/{placeId}")
    @Transactional(readOnly = true)
    public PlaceDto getPlaceById(@Positive @PathVariable int placeId) {
        log.info("Запрос на получение информации о месте {}", placeId);
        return placeService.getPlaceById(placeId);
    }
}