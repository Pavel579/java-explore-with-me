package ru.practicum.ewm.service.admin;

import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;

public interface AdminCompilationsService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    void deleteById(Long compId);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void removePinned(Long compId);

    void setPinned(Long compId);
}
