package ru.practicum.ewm.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.service.hits.HitService;
import ru.practicum.ewm.storage.EventRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;
    private final UserMapper userMapper;
    private final HitService hitService;

    public EventFullDto mapToEventFullDto(Event event, Long confirmedRequests, Long views) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.mapToCategoryDto(event.getCategory()),
                confirmedRequests,
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                userMapper.mapToUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                views
        );
    }

    public Event mapToEvent(NewEventDto newEventDto, Category category, User user, Location location) {
        return new Event(
                newEventDto.getId(),
                newEventDto.getAnnotation(),
                category,
                LocalDateTime.now(),
                user,
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                location,
                newEventDto.isPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.isRequestModeration(),
                EventState.PENDING,
                newEventDto.getTitle()
        );
    }

    public EventShortDto mapToEventShortDto(Event event, Long confirmedRequests) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.mapToCategoryDto(event.getCategory()),
                confirmedRequests,
                event.getEventDate(),
                userMapper.mapToUserShortDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                hitService.getViewsForEvent(event, false)
        );
    }

    public List<EventShortDto> mapToListEventShortDto(List<Event> eventList) {
        Map<Long, Long> confirmedRequests = getConfirmedRequests(eventList);
        return eventList.stream().map(event -> this.mapToEventShortDto(event, confirmedRequests.get(event.getId()))).collect(Collectors.toList());
    }

    public List<EventFullDto> mapToListEventFullDto(List<Event> eventList) {
        Map<Long, Long> confirmedRequests = getConfirmedRequests(eventList);
        Map<Long, Long> views = hitService.getViewsForEvents(eventList, false);
        return eventList.stream().map(event -> this.mapToEventFullDto(event, confirmedRequests.get(event.getId()), views.get(event.getId()))).collect(Collectors.toList());
    }

    public Map<Long, Long> getConfirmedRequests(List<Event> events) {
        Map<Long, Long> confirmedRequests = new HashMap<>();
        List<Long> eventsIdsList = events.stream().map(Event::getId).collect(Collectors.toList());
        for (Long[] counts : eventRepository.countAllConfirmedRequests(RequestState.CONFIRMED, eventsIdsList)) {
            confirmedRequests.put(counts[0], counts[1]);
        }
        return confirmedRequests;
    }
}
