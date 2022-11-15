package ru.practicum.ewm.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.List;

public interface PublicService {
    List<CategoryDto> getCategories(PageRequest pageRequest);

    CategoryDto getCategoryById(Long catId);

    CompilationDto getCompilationById(Long compId);

    EventFullDto getEventById(Long id);

    List<CompilationDto> getCompilationsByParams(Boolean pinned, PageRequest pageRequest);

    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                  String rangeStart, String rangeEnd, Boolean onlyAvailable, PageRequest pageRequest);
}
