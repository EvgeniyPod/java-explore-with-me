package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.CategoryService;
import ru.practicum.category.dto.CategoryDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/categories")
public class CategoryPrivateController {
    private final CategoryService categoryService;

    /** Метод для получения списка категорий. */
    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на получение списка категорий: from={} size={}", from, size);
        return categoryService.getCategories(from, size);
    }

    /** Метод для получения категории по идентификатору. */
    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@Positive @PathVariable int catId) {
        log.info("Запрос на получение категории с идентификатором {}", catId);
        return categoryService.getCategoryById(catId);
    }
}
