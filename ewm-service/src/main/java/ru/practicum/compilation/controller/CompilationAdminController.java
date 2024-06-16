package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.CompilationService;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;

    /** Метод для создания новой компиляции */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilation) {
        log.info("Запрос на создание компиляции {}", compilation.getTitle());
        return compilationService.createCompilation(compilation);
    }

    /** Метод для обновления компиляции по идентификатору */
    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@Positive @PathVariable int compId,
                                            @Valid @RequestBody UpdateCompilationRequest compilation) {
        log.info("Запрос на обновление компиляции {}", compId);
        return compilationService.updateCompilation(compId, compilation);
    }

    /** Метод для удаления компиляции по идентификатору */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@Positive @PathVariable int compId) {
        log.info("Запрос на удаление компиляции {}", compId);
        compilationService.deleteCompilation(compId);
    }
}