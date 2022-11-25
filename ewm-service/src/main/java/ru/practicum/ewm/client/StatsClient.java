package ru.practicum.ewm.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.model.EndpointHit;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsClient {
    @Value("${URL_STATS}")
    private String urlStats;
    private final WebClient webClient = WebClient.create("http://localhost:8080");

    public EndpointHit sendHit(EndpointHit endpointHit) {
        log.info("sendHit client start");
        return webClient
                .post()
                .uri(urlStats + "/hit")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(endpointHit))
                .retrieve()
                .bodyToMono(EndpointHit.class)
                .block();
    }
}
