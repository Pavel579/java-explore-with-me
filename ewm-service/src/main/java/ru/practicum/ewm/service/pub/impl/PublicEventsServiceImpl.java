package ru.practicum.ewm.service.pub.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.service.hits.HitService;
import ru.practicum.ewm.service.pub.PublicEventsService;
import ru.practicum.ewm.storage.EventRepository;
import ru.practicum.ewm.storage.RequestRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventsServiceImpl implements PublicEventsService {
    private final HitService hitService;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EventFullDto getById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found!"));
        //event.setViews(event.getViews() + 1);
        //int confirmedRequests = requestRepository.findConfirmedRequests(event.getId(), RequestState.CONFIRMED);
        Long confirmedRequests = requestRepository.findConfirmedRequests(event.getId(), RequestState.CONFIRMED);
        Long views = hitService.getViewsForEvent(event, false);
        return eventMapper.mapToEventFullDto(event, confirmedRequests, views);
    }

    @Override
    public List<EventShortDto> getAll(String text, List<Long> categories,
                                      Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      Boolean onlyAvailable, PageRequest pageRequest) {
        LocalDateTime start;
        LocalDateTime end;
        start = Objects.requireNonNullElseGet(rangeStart, () -> LocalDateTime.of(1970, 12, 2, 0, 0));
        end = Objects.requireNonNullElseGet(rangeEnd, () -> LocalDateTime.of(3000, 12, 2, 0, 0));

        Specification<Event> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Subquery<Long> subQuery = query.subquery(Long.class);
            Root<Request> requestRoot = subQuery.from(Request.class);
            Join<Request, Event> eventsRequests = requestRoot.join("event");

            predicates.add(builder.equal(root.get("state"), EventState.PUBLISHED));
            if (text != null && !text.isEmpty()) {
                predicates.add(builder.or(builder.like(builder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                        builder.like(builder.lower(root.get("description")), "%" + text.toLowerCase() + "%")));
            }
            if (categories != null) {
                predicates.add(builder.and(root.get("category").in(categories)));
            }
            if (paid != null) {
                predicates.add(builder.equal(root.get("paid"), paid));
            }
            predicates.add(builder.greaterThan(root.get("eventDate"), start));
            predicates.add(builder.lessThan(root.get("eventDate"), end));
            if (onlyAvailable != null && onlyAvailable) {
                predicates.add(builder.or(builder.equal(root.get("participantLimit"), 0),
                        builder.and(builder.notEqual(root.get("participantLimit"), 0),
                                builder.greaterThan(root.get("participantLimit"), subQuery.select(builder.count(requestRoot.get("id")))
                                        .where(builder.equal(eventsRequests.get("id"), requestRoot.get("event").get("id")))
                                        .where(builder.equal(requestRoot.get("status"), RequestState.CONFIRMED))))));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };

        List<Event> events = eventRepository.findAll(specification, pageRequest);

        return eventMapper.mapToListEventShortDto(events);
    }

    /*@Override
    public Map<Long, Long> getConfirmedRequests(List<Event> events) {
        Map<Long, Long> confirmedRequests = new HashMap<>();
        List<Long> eventsIdsList = events.stream().map(Event::getId).collect(Collectors.toList());
        for (Long[] counts : eventRepository.countAllConfirmedRequests(RequestState.CONFIRMED.toString(), eventsIdsList)) {
            confirmedRequests.put(counts[0], counts[1]);
        }
        return confirmedRequests;
    }*/
}
