package ru.practicum.ewm.service.admin;

import ru.practicum.ewm.dto.category.CategoryDto;

public interface AdminCategoriesService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void deleteById(Long catId);
}
