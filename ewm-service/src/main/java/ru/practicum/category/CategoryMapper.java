package ru.practicum.category;

import org.springframework.data.domain.Page;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    /** Преобразование идентификатора и объекта CategoryDto в объект Category. */
    public static Category toCategory(int categoryId, CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryId)
                .name(categoryDto.getName())
                .build();
    }

    /** Преобразование объектов Category и CategoryDto в объект Category. */
    public static Category toCategory(Category category, CategoryDto categoryDto) {
        return Category.builder()
                .id(category.getId())
                .name(categoryDto.getName())
                .build();
    }

    /** Преобразование объекта Page<Category> в список объектов CategoryDto. */
    public static List<CategoryDto> toCategoryDto(Page<Category> categoryPage) {
        return categoryPage.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    /** Преобразование объекта NewCategoryDto в объект Category. */
    public static Category toCategory(NewCategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    /** Преобразование объекта Category в объект CategoryDto. */
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
