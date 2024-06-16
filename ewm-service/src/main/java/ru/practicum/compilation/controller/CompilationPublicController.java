package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.CompilationService;
import ru.practicum.compilation.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/compilations")
public class CompilationPublicController {
    private final CompilationService compilationService;

    /** Метод для получения списка компиляций с возможностью фильтрации по прикреплённым */
    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "10") int size) {
        if (pinned == null) {
            log.info("Запрос на получение компиляций from={} size={}", from, size);
        } else if (pinned) {
            log.info("Запрос на получение закреплённых компиляций from={} size={}", from, size);
        } else {
            log.info("Запрос на получение не закреплённых компиляций from={} size={}", from, size);
        }
        return compilationService.getCompilations(pinned, from, size);
    }

    /** Метод для получения информации о компиляции по её идентификатору */
    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@Positive @PathVariable int compId) {
        log.info("Запрос на получение информации о компиляции {}", compId);
        return compilationService.getCompilationById(compId);
    }
}