package ru.practicum.ewm.service.priv.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.exceptions.RequestAlreadyExistsException;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.service.priv.PrivateRequestsService;
import ru.practicum.ewm.storage.EventRepository;
import ru.practicum.ewm.storage.RequestRepository;
import ru.practicum.ewm.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateRequestsServiceImpl implements PrivateRequestsService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;


    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        Request request = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        Long confirmedRequests = requestRepository.findConfirmedRequests(event.getId(), RequestState.CONFIRMED);
        if (request != null) {
            throw new RequestAlreadyExistsException("Request already exists!");
        }
        if (event.getInitiator().getId().equals(userId) || !event.getState().equals(EventState.PUBLISHED)
                || (event.getParticipantLimit() > 0 && event.getParticipantLimit() - confirmedRequests <= 0)) {
            throw new RequestAlreadyExistsException("Bad request!!");
        }
        //event.setConfirmedRequests(confirmedRequests);
        Request saveRequest = requestRepository.save(requestMapper.mapToRequest(user, event));
        return requestMapper.mapToParticipationRequestDto(saveRequest);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        List<Request> userRequests = requestRepository.findAllByRequesterId(userId);
        return requestMapper.mapToListParticipationRequestDto(userRequests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request Not found!"));
        request.setStatus(RequestState.CANCELED);
        /*if (request.getRequester().getId().equals(userId)) {
            request.setStatus(RequestState.CANCELED);
            //requestRepository.save(request);
            return requestMapper.mapToParticipationRequestDto(request);
        } else {
            throw new RequestAlreadyExistsException("Can't cancel request");
        }*/
        return requestMapper.mapToParticipationRequestDto(request);
    }
}
