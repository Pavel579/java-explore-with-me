package ru.practicum.ewm.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public Category mapToCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName()
        );
    }

    public CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public List<CategoryDto> mapToListCategory(Page<Category> categories) {
        return categories.stream().map(this::mapToCategoryDto).collect(Collectors.toList());
    }
}
