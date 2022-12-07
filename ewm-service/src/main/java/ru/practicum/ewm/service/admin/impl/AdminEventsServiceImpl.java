package ru.practicum.ewm.service.admin.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.AdminUpdateEventRequestDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.service.admin.AdminCompilationsService;
import ru.practicum.ewm.service.admin.AdminEventsService;
import ru.practicum.ewm.service.hits.HitService;
import ru.practicum.ewm.storage.EventRepository;
import ru.practicum.ewm.storage.RequestRepository;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ru.practicum.ewm.utils.Utils.getNullPropertyNames;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminEventsServiceImpl implements AdminEventsService {
    private final HitService hitService;
    private final AdminCompilationsService adminCompilationsService;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private LocalDateTime start;
    private LocalDateTime end;

    @Override
    @Transactional
    public EventFullDto publish(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.getEventDate().minusHours(1).isAfter(LocalDateTime.now()) && event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
            Long confirmedRequests = requestRepository.findConfirmedRequests(event.getId(), RequestState.CONFIRMED);
            Long views = hitService.getViewsForEvent(event, false);
            return eventMapper.mapToEventFullDto(event, confirmedRequests, views, null);
        } else {
            throw new NotFoundException("Can't publish event");
        }
    }

    @Override
    @Transactional
    public EventFullDto reject(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found!"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            event.setState(EventState.CANCELED);
            Long confirmedRequests = requestRepository.findConfirmedRequests(event.getId(), RequestState.CONFIRMED);
            Long views = hitService.getViewsForEvent(event, false);
            return eventMapper.mapToEventFullDto(event, confirmedRequests, views, null);
        } else {
            throw new NotFoundException("Can't reject event");
        }
    }

    @Override
    public List<EventFullDto> getByParams(List<Long> users, List<EventState> states, List<Long> categories,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          PageRequest pageRequest) {
        log.debug("get events start service");
        start = Objects.requireNonNullElseGet(rangeStart, () -> LocalDateTime.of(1970, 12, 2, 0, 0));
        end = Objects.requireNonNullElseGet(rangeEnd, () -> LocalDateTime.of(3000, 12, 2, 0, 0));

        Specification<Event> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (users != null && !users.isEmpty()) {
                predicates.add(builder.and(root.get("initiator").in(users)));
            }
            if (states != null && !states.isEmpty()) {
                predicates.add(builder.and(root.get("state").in(states)));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(builder.and(root.get("category").in(categories)));
            }
            log.info("cr builder");
            predicates.add(builder.greaterThan(root.get("eventDate"), start));
            log.info("cr builder start after");
            predicates.add(builder.lessThan(root.get("eventDate"), end));

            return builder.and(predicates.toArray(new Predicate[0]));
        };

        List<Event> events = eventRepository.findAll(specification, pageRequest);

        Map<Long, Long> confirmedRequests = adminCompilationsService.getConfirmedRequests(events);
        Map<Long, Long> views = hitService.getViewsForEvents(events, false);
        return eventMapper.mapToListEventFullDto(events, confirmedRequests, views);
    }

    @Override
    @Transactional
    public EventFullDto update(Long eventId, AdminUpdateEventRequestDto adminUpdateEventRequestDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found!"));
        BeanUtils.copyProperties(adminUpdateEventRequestDto, event, getNullPropertyNames(adminUpdateEventRequestDto));
        Long confirmedRequests = requestRepository.findConfirmedRequests(event.getId(), RequestState.CONFIRMED);
        Long views = hitService.getViewsForEvent(event, false);
        return eventMapper.mapToEventFullDto(event, confirmedRequests, views, null);
    }
}
