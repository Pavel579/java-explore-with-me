package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.weather.Weather;

@Service
public class WeatherClient {
    private final WebClient webClient = WebClient.create();
    @Value("${URL_WEATHER}")
    private String urlWeather;
    @Value("${WEATHER_KEY}")
    private String weatherKey;

    public Weather getCurrentWeather(Location location) {
        return webClient
                .get()
                .uri(urlWeather + "?lat=" + location.getLat() + "&lon=" + location.getLon() + "&lang=ru_RU&limit=1")
                .header("X-Yandex-API-Key", weatherKey)
                .retrieve()
                .bodyToMono(Weather.class)
                .block();
    }
}
