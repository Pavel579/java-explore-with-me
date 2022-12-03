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
//@RequiredArgsConstructor
public class StatsClient {
    private final WebClient webClient = WebClient.create();
    //private final WebClient webClient;
    @Value("${URL_STATS}")
    private String urlStats;
    @Value("${WEB_CLIENT_URL}")
    private String urlWebClient;

    /*@Autowired
    public StatsClient() {
        webClient = WebClient.builder()
                .baseUrl(urlStats)
                .build();
    }*/


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

    public List<ViewStatsDto> get(String start, String end, List<String> uris, Boolean unique) {
        log.info("get client start");
        return webClient
                .get()
                .uri(urlStats + "/stats?start=" + start + "&end=" + end + "&uris=" + String.join(", ", uris).replace("{", "").replace("}", "") + "&unique=" + unique)
                /*.uri(uriBuilder -> uriBuilder
                        .path("/stats/")
                        //.queryParam("start", start)
                        //.queryParam("end", end)
                        //.queryParam("uris", String.join(", ", uris).replace("{", "").replace("}", ""))
                        //.queryParam("unique", unique)
                        .build())*/
                //.header("Content-Type", "application/json")
                .retrieve()
                .bodyToFlux(ViewStatsDto.class)
                .collect(Collectors.toList())
                .block();
    }
}
