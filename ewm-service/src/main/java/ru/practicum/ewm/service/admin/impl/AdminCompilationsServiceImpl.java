package ru.practicum.ewm.service.admin.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.service.admin.AdminCompilationsService;
import ru.practicum.ewm.service.hits.HitService;
import ru.practicum.ewm.storage.CompilationRepository;
import ru.practicum.ewm.storage.EventRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final EventMapper eventMapper;
    private final CompilationMapper compilationMapper;
    private final HitService hitService;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Set<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.mapToCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        Map<Long, Long> confirmedRequests = getConfirmedRequests(new ArrayList<>(events));
        Map<Long, Long> views = hitService.getViewsForEvents(new ArrayList<>(events), false);
        List<EventShortDto> shortEvents = eventMapper.mapToListEventShortDto(new ArrayList<>(events), confirmedRequests, views);
        return compilationMapper.mapToCompilationDto(compilation, shortEvents);
    }

    @Override
    @Transactional
    public void deleteById(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found!"));
        Set<Event> events = compilation.getEvents();
        events.removeIf(e -> e.getId().equals(eventId));
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found!"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found!"));
        compilation.getEvents().add(event);

    }

    @Override
    @Transactional
    public void removePinned(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found!"));
        compilation.setPinned(false);
    }

    @Override
    @Transactional
    public void setPinned(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found!"));
        compilation.setPinned(true);
    }

    public Map<Long, Long> getConfirmedRequests(List<Event> events) {
        Map<Long, Long> confirmedRequests = new HashMap<>();
        List<Long> eventsIdsList = events.stream().map(Event::getId).collect(Collectors.toList());
        List<Long[]> list = eventRepository.countAllConfirmedRequests(RequestState.CONFIRMED, eventsIdsList);
        for (Long[] counts : list) {
            confirmedRequests.put(counts[0], counts[1]);
        }
        return confirmedRequests;
    }
}
