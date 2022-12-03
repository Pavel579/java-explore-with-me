package ru.practicum.ewm.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.model.EndpointHit;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatsClient {
    private final WebClient webClient = WebClient.create();
    @Value("${URL_STATS}")
    private String urlStats;

    public EndpointHit sendHit(EndpointHit endpointHit) {
        log.debug("sendHit client start");
        return webClient
                .post()
                .uri(urlStats + "/hit")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(endpointHit))
                .retrieve()
                .bodyToMono(EndpointHit.class)
                .block();
    }

    public List<ViewStatsDto> get(String start, String end, List<String> uris, Boolean unique) {
        log.debug("get client start");
        return webClient
                .get()
                .uri(urlStats + "/stats?start=" + start + "&end=" + end + "&uris=" +
                        String.join(", ", uris).replace("{", "")
                                .replace("}", "") + "&unique=" + unique)
                .retrieve()
                .bodyToFlux(ViewStatsDto.class)
                .collect(Collectors.toList())
                .block();
    }
}
