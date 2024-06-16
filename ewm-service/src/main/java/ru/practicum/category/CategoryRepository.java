package ru.practicum.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    void deleteById(int userId);

    Optional<Category> findById(int categoryId);
}