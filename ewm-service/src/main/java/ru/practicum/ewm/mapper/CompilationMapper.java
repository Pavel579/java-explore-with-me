package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.List;
import java.util.Set;

@Component
public class CompilationMapper {
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
}
