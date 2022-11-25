package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.model.EndpointHit;

import java.util.List;

public interface StatsService {
    EndpointHit createHit(EndpointHit endpointHit);

    List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique);
}
