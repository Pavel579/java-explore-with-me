package ru.practicum.ewm.service.admin;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.event.AdminUpdateEventRequestDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventsService {
    EventFullDto publish(Long eventId);

    EventFullDto reject(Long eventId);

    List<EventFullDto> getByParams(List<Long> users, List<EventState> states,
                                   List<Long> categories, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, PageRequest pageRequest);

    EventFullDto update(Long eventId, AdminUpdateEventRequestDto adminUpdateEventRequestDto);
}
