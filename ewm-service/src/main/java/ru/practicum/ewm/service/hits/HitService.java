package ru.practicum.ewm.service.hits;

import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface HitService {
    EndpointHit sendHits(HttpServletRequest request);

    Long getViewsForEvent(Event event, Boolean unique);
    Map<Long, Long> getViewsForEvents(List<Event> event, Boolean unique);
}
