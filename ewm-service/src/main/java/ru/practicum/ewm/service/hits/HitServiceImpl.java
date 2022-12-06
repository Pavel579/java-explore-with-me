package ru.practicum.ewm.service.hits;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HitServiceImpl implements HitService {
    private final StatsClient statsClient;
    @Value("${EVENT_VIEW_URI}")
    private String eventViewUri;
    @Value("${APP_NAME}")
    private String appName;

    @Override
    public EndpointHit sendHits(HttpServletRequest request) {
        log.debug("hit service send hit");
        EndpointHit endpointHit = new EndpointHit(
                null,
                appName,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        return statsClient.sendHit(endpointHit);
    }

    @Override
    public Long getViewsForEvent(Event event, Boolean unique) {
        String start = event.getCreatedOn().withNano(0).toString().replace("T", " ");
        String end = LocalDateTime.now().withNano(0).toString().replace("T", " ");
        List<ViewStatsDto> views = statsClient.get(start, end,
                List.of(String.format(eventViewUri, event.getId())),
                unique);
        if (views.isEmpty()) {
            return 0L;
        }
        return views.get(0).getHits();
    }

    @Override
    public Map<Long, Long> getViewsForEvents(List<Event> events, Boolean unique) {
        Optional<Event> firstEvent = events.stream()
                .min(Comparator.comparing(Event::getEventDate));

        if (firstEvent.isEmpty()) {
            return Collections.emptyMap();
        }
        String start = firstEvent.get().getCreatedOn().withNano(0).toString().replace("T", " ");
        String end = LocalDateTime.now().withNano(0).toString().replace("T", " ");
        List<String> uris = events.stream()
                .map(event -> String.format(eventViewUri, event.getId()))
                .collect(Collectors.toList());
        List<ViewStatsDto> viewStatsDtos = statsClient.get(start, end, uris, unique);
        Map<Long, Long> eventsViews = new HashMap<>();

        viewStatsDtos.forEach(view -> eventsViews.put(extractNumber(view.getUri()), view.getHits()));

        return eventsViews;
    }

    private Long extractNumber(final String str) {

        if (str == null || str.isEmpty()) return null;

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
                found = true;
            } else if (found) {
                break;
            }
        }

        return Long.parseLong(sb.toString());
    }
}
