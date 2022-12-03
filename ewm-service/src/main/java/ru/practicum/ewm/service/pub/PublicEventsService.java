package ru.practicum.ewm.service.pub;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PublicEventsService {
    EventFullDto getById(Long id);

    List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, PageRequest pageRequest);

    //Map<Long, Long> getConfirmedRequests(List<Event> events);
}
