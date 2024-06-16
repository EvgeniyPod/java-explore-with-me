package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.CategoryService;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    /** Создание новой категории. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("Запрос на создание категории {} ", categoryDto.getName());
        return categoryService.createCategory(categoryDto);
    }

    /** Обновление существующей категории по идентификатору. */
    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@Positive @PathVariable int catId, @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Запрос на обновление категории {} ", catId);
        return categoryService.updateCategory(catId, categoryDto);
    }

    /** Удаление категории по идентификатору. */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Positive @PathVariable int catId) {
        log.info("Запрос на удаление категории {} ", catId);
        categoryService.deleteCategory(catId);
    }
}
