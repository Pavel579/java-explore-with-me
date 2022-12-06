package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestMapper {
    public Request mapToRequest(User user, Event event) {
        return new Request(
                null,
                LocalDateTime.now(),
                event,
                user,
                RequestState.PENDING
                );
    }

    public ParticipationRequestDto mapToParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }

    public List<ParticipationRequestDto> mapToListParticipationRequestDto(List<Request> requestList) {
        return requestList.stream().map(this::mapToParticipationRequestDto).collect(Collectors.toList());
    }
}
