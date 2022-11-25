package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.service.HitService;
import ru.practicum.ewm.service.PublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicController {
    private final PublicService publicService;
    private final HitService hitService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        PageRequest pageRequest = null;
        if (from != null && size != null) {
            int page = from / size;
            pageRequest = PageRequest.of(page, size);
        }
        return publicService.getCategories(pageRequest);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        return publicService.getCategoryById(catId);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        return publicService.getCompilationById(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilationsByParams(
            @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        PageRequest pageRequest = null;
        if (from != null && size != null) {
            int page = from / size;
            pageRequest = PageRequest.of(page, size);
        }
        return publicService.getCompilationsByParams(pinned, pageRequest);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        hitService.sendHits(request);
        return publicService.getEventById(id);
    }

    @GetMapping("/events")
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {
        PageRequest pageRequest = null;
        if (from != null && size != null) {
            int page = from / size;
            pageRequest = PageRequest.of(page, size);
            if (sort != null) {
                Sort sorting;
                switch (sort) {
                    case "EVENT_DATE":
                        sorting = Sort.by(Sort.Direction.DESC, "eventDate");
                        break;
                    case "VIEWS":
                        sorting = Sort.by(Sort.Direction.DESC, "views");
                        break;
                    default:
                        sorting = Sort.by(Sort.Direction.DESC, "id");
                }
                pageRequest = PageRequest.of(page, size, sorting);
            }
        }
        hitService.sendHits(request);
        return publicService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageRequest);
    }
}
