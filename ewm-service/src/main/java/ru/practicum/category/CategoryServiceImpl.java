package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.InvalidEventStateOrDate;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    /** Метод для создания новой категории */
    @Override
    public CategoryDto createCategory(NewCategoryDto category) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(category)));
    }

    /** Метод для обновления информации о категории */
    @Override
    public CategoryDto updateCategory(int categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException(categoryId,
                        "Категория с идентификатором " + categoryId + " не найдена"));

        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(category, categoryDto)));
    }

    /** Метод для удаления категории по идентификатору */
    @Override
    public void deleteCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException(categoryId,
                        "Категория с идентификатором " + categoryId + " не найдена"));
        if (!eventRepository.findByCategoryId(categoryId).isEmpty()) {
            throw new InvalidEventStateOrDate("Категория не пуста и не может быть удалена");
        }

        categoryRepository.deleteById(categoryId);
    }

    /** Метод для получения списка категорий с пагинацией */
    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        final Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return CategoryMapper.toCategoryDto(categoryRepository.findAll(page));
    }

    /** Метод для получения категории по идентификатору */
    @Override
    public CategoryDto getCategoryById(int categoryId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException(categoryId,
                        "Категория с идентификатором " + categoryId + " не найдена")));
    }
}
