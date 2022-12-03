package ru.practicum.ewm.service.pub.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.service.pub.PublicCategoriesService;
import ru.practicum.ewm.storage.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCategoriesServiceImpl implements PublicCategoriesService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getAll(PageRequest pageRequest) {
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categoryMapper.mapToListCategory(categories);
    }

    @Override
    public CategoryDto getById(Long catId) {
        return categoryMapper.mapToCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found")));
    }
}
