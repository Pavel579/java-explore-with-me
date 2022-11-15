package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.exceptions.RequestAlreadyExistsException;
import ru.practicum.ewm.exceptions.RequestNotFoundException;
import ru.practicum.ewm.exceptions.UserNotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.storage.EventRepository;
import ru.practicum.ewm.storage.LocationRepository;
import ru.practicum.ewm.storage.RequestRepository;
import ru.practicum.ewm.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateServiceImpl implements PrivateService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final AdminService adminService;
    private final PublicService publicService;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User user = userMapper.mapToUser(adminService.getUserById(userId));
        Category category = categoryMapper.mapToCategory(publicService.getCategoryById(newEventDto.getCategory()));
        Location location = locationRepository.save(newEventDto.getLocation());
        return eventMapper.mapToEventFullDto(eventRepository.save(eventMapper.mapToEvent(newEventDto, category, user, location)));
    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, PageRequest pageRequest) {
        List<Event> event = eventRepository.findAllByInitiatorId(userId, pageRequest);
        return eventMapper.mapToListEventShortDto(event);
    }

    @Override
    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        Event event = eventRepository.findAllByInitiatorIdAndId(userId, eventId);
        return eventMapper.mapToEventFullDto(event);
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Request request = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (request != null) {
            throw new RequestAlreadyExistsException("Request already exists!");
        }
        if (event.getInitiator().getId().equals(userId) || /*!event.getState().equals(EventState.PUBLISHED)
                ||*/ (event.getParticipantLimit() > 0 && event.getConfirmedRequests() != null && event.getParticipantLimit() - event.getConfirmedRequests() <= 0)) {
            throw new RequestAlreadyExistsException("Bad request!!");
        }

        Request saveRequest = requestRepository.save(requestMapper.mapToRequest(user,event));
        return requestMapper.mapToParticipationRequestDto(saveRequest);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        List<Request> userRequests = requestRepository.findAllByRequesterId(userId);
        return requestMapper.mapToListParticipationRequestDto(userRequests);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException("Request Not found!"));
        if (request.getRequester().getId().equals(userId)){
            request.setStatus(RequestState.CANCELED);
            requestRepository.save(request);
            return requestMapper.mapToParticipationRequestDto(request);
        }else {
            throw new RequestAlreadyExistsException("Pizdec");
        }
    }
}
