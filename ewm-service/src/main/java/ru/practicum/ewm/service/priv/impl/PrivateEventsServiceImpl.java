package ru.practicum.ewm.service.priv.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventRequestDto;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.exceptions.ValidationException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.service.admin.AdminCompilationsService;
import ru.practicum.ewm.service.admin.AdminUsersService;
import ru.practicum.ewm.service.hits.HitService;
import ru.practicum.ewm.service.priv.PrivateEventsService;
import ru.practicum.ewm.service.pub.PublicCategoriesService;
import ru.practicum.ewm.storage.EventRepository;
import ru.practicum.ewm.storage.LocationRepository;
import ru.practicum.ewm.storage.RequestRepository;
import ru.practicum.ewm.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.ewm.utils.Utils.getNullPropertyNames;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PrivateEventsServiceImpl implements PrivateEventsService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final AdminUsersService adminUsersService;
    private final AdminCompilationsService adminCompilationsService;
    private final PublicCategoriesService publicCategoriesService;
    private final HitService hitService;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        User user = userMapper.mapToUser(adminUsersService.getById(userId));
        Category category = categoryMapper.mapToCategory(publicCategoriesService.getById(newEventDto.getCategory()));
        Location location = locationRepository.save(newEventDto.getLocation());
        Event event = eventRepository.save(eventMapper.mapToEvent(newEventDto, category, user, location));
        log.debug("priv service create end");
        Long confirmedRequests = requestRepository.findConfirmedRequests(event.getId(), RequestState.CONFIRMED);
        Long views = hitService.getViewsForEvent(event, false);
        return eventMapper.mapToEventFullDto(event, confirmedRequests, views);
    }

    @Override
    public List<EventShortDto> getByUser(Long userId, PageRequest pageRequest) {
        List<Event> event = eventRepository.findAllByInitiatorId(userId, pageRequest);
        Map<Long, Long> confirmedRequests = adminCompilationsService.getConfirmedRequests(event);
        Map<Long, Long> views = hitService.getViewsForEvents(event, false);
        return eventMapper.mapToListEventShortDto(event, confirmedRequests, views);
    }

    @Override
    public EventFullDto getByUserIdAndEventId(Long userId, Long eventId) {
        Event event = eventRepository.findAllByInitiatorIdAndId(userId, eventId);
        Long confirmedRequests = requestRepository.findConfirmedRequests(eventId, RequestState.CONFIRMED);
        Long views = hitService.getViewsForEvent(event, false);
        return eventMapper.mapToEventFullDto(event, confirmedRequests, views);
    }


    @Override
    public List<ParticipationRequestDto> getParticipationRequestsOfUser(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findAllByRequesterIdAndEventId(userId, eventId);
        return requestMapper.mapToListParticipationRequestDto(requests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long reqId) {
        Request request = requestRepository.findByIdAndEventIdAndEventInitiatorId(reqId, eventId, userId).orElseThrow(() -> new NotFoundException("No request"));
        Event event = request.getEvent();
        Long confirmedRequests = requestRepository.findConfirmedRequests(eventId, RequestState.CONFIRMED);
        if (!userId.equals(event.getInitiator().getId()) || !request.getEvent().getId().equals(eventId)) {
            throw new ForbiddenException("Incorrect data");
        }
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return requestMapper.mapToParticipationRequestDto(request);
        }
        if (confirmedRequests == event.getParticipantLimit()) {
            throw new ValidationException("Достигнут лимит по участию в эвенте");
        }
        request.setStatus(RequestState.CONFIRMED);

        if (confirmedRequests == event.getParticipantLimit()) {
            List<Request> toCancelRequests = requestRepository.findAllByEventIdAndStatus(eventId, RequestState.PENDING);
            for (Request toCancelRequest : toCancelRequests) {
                toCancelRequest.setStatus(RequestState.REJECTED);
            }
        }
        return requestMapper.mapToParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId) {
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        if (userId.equals(event.getInitiator().getId())) {
            request.setStatus(RequestState.REJECTED);
        } else {
            throw new ForbiddenException("Incorrect data");
        }
        return requestMapper.mapToParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public EventFullDto update(UpdateEventRequestDto updateEventDto, Long userId) {
        Event event = eventRepository.findById(updateEventDto.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        if (userId.equals(event.getInitiator().getId()) && (event.getState().equals(EventState.PENDING) ||
                event.getState().equals(EventState.CANCELED) ||
                event.getEventDate().isAfter(LocalDateTime.now().plusHours(2)))) {
            if (event.getState().equals(EventState.CANCELED)) {
                event.setState(EventState.PENDING);
            }
            BeanUtils.copyProperties(updateEventDto, event, getNullPropertyNames(updateEventDto));
        } else {
            throw new ForbiddenException("Incorrect data");
        }
        Long confirmedRequests = requestRepository.findConfirmedRequests(event.getId(), RequestState.CONFIRMED);
        Long views = hitService.getViewsForEvent(event, false);
        return eventMapper.mapToEventFullDto(event, confirmedRequests, views);
    }

    @Override
    @Transactional
    public EventFullDto cancel(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.CANCELED);
        } else {
            throw new ForbiddenException("Incorrect data");
        }
        Long confirmedRequests = requestRepository.findConfirmedRequests(event.getId(), RequestState.CONFIRMED);
        Long views = hitService.getViewsForEvent(event, false);
        return eventMapper.mapToEventFullDto(event, confirmedRequests, views);
    }
}
