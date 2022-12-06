package ru.practicum.ewm.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation mapToCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        return new Compilation(
                null,
                events,
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle()
        );
    }

    public CompilationDto mapToCompilationDto(Compilation compilation, List<EventShortDto> shortEvents) {
        return new CompilationDto(
                compilation.getId(),
                shortEvents,
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    public List<CompilationDto> mapToListCompilationDto(List<Compilation> compilations, Map<Long, Long> confirmedRequests, Map<Long, Long> views) {
        Map<Long, Set<Event>> events = new HashMap<>();
        for (Compilation compilation : compilations) {
            events.put(compilation.getId(), compilation.getEvents());
        }
        return compilations.stream().map(compilation -> this
                        .mapToCompilationDto(compilation, eventMapper.mapToListEventShortDto(new ArrayList<>(events.get(compilation.getId())),
                                confirmedRequests, views))).collect(Collectors.toList());
    }
}
