package ru.practicum.ewm.service.pub;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {
    CompilationDto getById(Long compId);

    List<CompilationDto> getByParams(Boolean pinned, PageRequest pageRequest);
}
