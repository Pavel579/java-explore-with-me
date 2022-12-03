package ru.practicum.ewm.service.pub.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.service.pub.PublicCompilationsService;
import ru.practicum.ewm.storage.CompilationRepository;
import ru.practicum.ewm.storage.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationsServiceImpl implements PublicCompilationsService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Override
    public CompilationDto getById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found!"));
        List<Long> eventsIds = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
        List<Event> events = eventRepository.findAllById(eventsIds);
        List<EventShortDto> eventsShort = eventMapper.mapToListEventShortDto(events);
        return compilationMapper.mapToCompilationDto(compilation, eventsShort);
    }

    @Override
    public List<CompilationDto> getByParams(Boolean pinned, PageRequest pageRequest) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequest);
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<EventShortDto> shortEvents = eventMapper.mapToListEventShortDto(new ArrayList<>(compilation.getEvents()));
            result.add(compilationMapper.mapToCompilationDto(compilation, shortEvents));
        }
        return result;
    }
}
