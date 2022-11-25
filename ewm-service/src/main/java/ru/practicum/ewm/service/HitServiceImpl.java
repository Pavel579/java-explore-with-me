package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.model.EndpointHit;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class HitServiceImpl implements HitService {
    private final StatsClient statsClient;

    @Override
    public EndpointHit sendHits(HttpServletRequest request) {
        log.debug("hit service send hit");
        EndpointHit endpointHit = new EndpointHit(
                null,
                "ewm-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        return statsClient.sendHit(endpointHit);
    }
}
