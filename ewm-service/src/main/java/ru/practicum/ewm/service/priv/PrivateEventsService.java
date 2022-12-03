package ru.practicum.ewm.service.priv;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventRequestDto;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventsService {
    EventFullDto create(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getByUser(Long userId, PageRequest pageRequest);

    EventFullDto getByUserIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipationRequestsOfUser(Long userId, Long eventId);

    ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId);

    EventFullDto update(UpdateEventRequestDto updateEventDto, Long userId);

    EventFullDto cancel(Long userId, Long eventId);
}
