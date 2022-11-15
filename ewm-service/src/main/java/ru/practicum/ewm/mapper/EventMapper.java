package ru.practicum.ewm.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public EventFullDto mapToEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.mapToCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                userMapper.mapToUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public Event mapToEvent(NewEventDto newEventDto, Category category, User user, Location location) {
        return new Event(
                newEventDto.getId(),
                newEventDto.getAnnotation(),
                category,
                LocalDateTime.now(),
                0,
                user,
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                location,
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.getRequestModeration(),
                EventState.PENDING,
                newEventDto.getTitle(),
                0
        );
    }

    public EventShortDto mapToEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.mapToCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                userMapper.mapToUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public List<EventShortDto> mapToListEventShortDto(List<Event> eventList) {
        return eventList.stream().map(this::mapToEventShortDto).collect(Collectors.toList());
    }

    public List<EventShortDto> mapToListEventShortDto(Page<Event> eventList) {
        return eventList.stream().map(this::mapToEventShortDto).collect(Collectors.toList());
    }

    public List<EventFullDto> mapToListEventFullDto(List<Event> events) {
        return events.stream().map(this::mapToEventFullDto).collect(Collectors.toList());
    }

    public List<EventFullDto> mapToListEventFullDto(Page<Event> events) {
        return events.stream().map(this::mapToEventFullDto).collect(Collectors.toList());
    }
}
