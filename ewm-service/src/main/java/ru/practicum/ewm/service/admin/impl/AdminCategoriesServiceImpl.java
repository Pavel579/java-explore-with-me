package ru.practicum.ewm.service.admin.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.service.admin.AdminCategoriesService;
import ru.practicum.ewm.service.pub.PublicCategoriesService;
import ru.practicum.ewm.storage.CategoryRepository;
import ru.practicum.ewm.storage.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final PublicCategoriesService publicCategoriesService;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto create(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.mapToCategory(categoryDto));
        return categoryMapper.mapToCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.mapToCategory(categoryDto));
        return categoryMapper.mapToCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteById(Long catId) {
        List<Event> events = eventRepository.findAllByCategoryId(catId);
        publicCategoriesService.getById(catId);
        if (events.isEmpty()) {
            categoryRepository.deleteById(catId);
        } else {
            throw new ForbiddenException("Event has this category");
        }
    }
}
