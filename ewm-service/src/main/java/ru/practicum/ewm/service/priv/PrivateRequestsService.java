package ru.practicum.ewm.service.priv;

import ru.practicum.ewm.dto.request.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestsService {
    ParticipationRequestDto create(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto cancel(Long userId, Long requestId);
}
