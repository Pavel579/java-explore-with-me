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
import ru.practicum.ewm.service.admin.AdminCompilationsService;
import ru.practicum.ewm.storage.CompilationRepository;
import ru.practicum.ewm.storage.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final EventMapper eventMapper;
    private final CompilationMapper compilationMapper;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.mapToCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        List<EventShortDto> shortEvents = eventMapper.mapToListEventShortDto(events);
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
        List<Event> events = compilation.getEvents();
        events.removeIf(e -> e.getId().equals(eventId));
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found!"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found!"));
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);

    }

    @Override
    @Transactional
    public void removePinned(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found!"));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void setPinned(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found!"));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}
