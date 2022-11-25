package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.storage.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public EndpointHit createHit(EndpointHit endpointHit) {
        log.info("create hit service");
        statsRepository.save(endpointHit);
        return endpointHit;
    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startFrom = LocalDateTime.parse(start, format);
        LocalDateTime endBefore = LocalDateTime.parse(end, format);
        if (uris == null || uris.isEmpty()) {
            return Collections.emptyList();
        }
        if (!unique) {
            return statsRepository.findAll(startFrom, endBefore, uris);
        } else {
            return statsRepository.findAllUnique(startFrom, endBefore, uris);
        }
    }
}
