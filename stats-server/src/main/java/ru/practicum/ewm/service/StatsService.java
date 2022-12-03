package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHit createHit(EndpointHit endpointHit);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
