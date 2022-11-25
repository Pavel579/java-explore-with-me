package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.storage.CategoryRepository;
import ru.practicum.ewm.storage.CompilationRepository;
import ru.practicum.ewm.storage.EventRepository;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {
    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Override
    public List<CategoryDto> getCategories(PageRequest pageRequest) {
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categoryMapper.mapToListCategory(categories);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return categoryMapper.mapToCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found")));
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found!"));
        List<Long> eventsIds = new ArrayList<>();
        for (Event e : compilation.getEvents()) {
            eventsIds.add(e.getId());
        }
        List<Event> events = eventRepository.findAllById(eventsIds);
        List<EventShortDto> eventsShort = eventMapper.mapToListEventShortDto(events);
        return compilationMapper.mapToCompilationDto(compilation, eventsShort);
    }

    @Override
    public List<CompilationDto> getCompilationsByParams(Boolean pinned, PageRequest pageRequest) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequest);
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<EventShortDto> shortEvents = eventMapper.mapToListEventShortDto(compilation.getEvents());
            result.add(compilationMapper.mapToCompilationDto(compilation, shortEvents));
        }
        return result;
    }

    @Override
    public EventFullDto getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found!"));
        event.setViews(event.getViews() + 1);
        return eventMapper.mapToEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories,
                                         Boolean paid, String rangeStart, String rangeEnd,
                                         Boolean onlyAvailable, PageRequest pageRequest) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start;
        LocalDateTime end;
        if (rangeStart == null) {
            start = LocalDateTime.of(1970, 12, 2, 0, 0);
        } else {
            start = LocalDateTime.parse(rangeStart, format);
        }
        if (rangeEnd == null) {
            end = LocalDateTime.of(3000, 12, 2, 0, 0);
        } else {
            end = LocalDateTime.parse(rangeEnd, format);
        }

        Specification<Event> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(builder.equal(root.get("state"), EventState.PUBLISHED));
            if (text != null && !text.isEmpty()) {
                predicates.add(builder.or(builder.like(builder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                        builder.like(builder.lower(root.get("description")), "%" + text.toLowerCase() + "%")));
            }
            if (categories != null) {
                predicates.add(builder.and(root.get("category").in(categories)));
            }
            if (paid != null) {
                predicates.add(builder.equal(root.get("paid"), paid));
            }
            predicates.add(builder.greaterThan(root.get("eventDate"), start));
            predicates.add(builder.lessThan(root.get("eventDate"), end));
            if (onlyAvailable != null) {
                predicates.add(builder.or(builder.equal(root.get("participantLimit"), 0),
                        builder.and(builder.notEqual(root.get("participantLimit"), 0),
                                builder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests")))));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Event> events = eventRepository.findAll(specification, pageRequest);

        return eventMapper.mapToListEventShortDto(events);
    }
}
