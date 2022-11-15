package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import ru.practicum.ewm.storage.CategoryRepository;
import ru.practicum.ewm.storage.CompilationRepository;
import ru.practicum.ewm.storage.EventRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CategoryDto> getCategories(PageRequest pageRequest) {
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categoryMapper.mapToListCategory(categories);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return categoryMapper.mapToCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found")));
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found!"));
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
        return eventMapper.mapToEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories,
                                         Boolean paid, String rangeStart, String rangeEnd,
                                         Boolean onlyAvailable, String sort, PageRequest pageRequest) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(rangeStart, format);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, format);
        Sort sort1 = null;
        switch (sort) {
            case "EVENT_DATE":
                sort1 = Sort.by(Sort.Direction.DESC, "eventDate");
                break;
            case "VIEWS":
                sort1 = Sort.by(Sort.Direction.DESC, "views");
                break;
        }
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        criteriaQuery.select(root);


        return null;
    }
}
