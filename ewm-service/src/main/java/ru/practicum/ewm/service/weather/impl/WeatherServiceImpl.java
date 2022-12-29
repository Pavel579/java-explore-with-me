package ru.practicum.ewm.service.weather.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.WeatherClient;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.weather.WeatherResponseDto;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.WeatherMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.weather.Weather;
import ru.practicum.ewm.service.weather.WeatherService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WeatherClient weatherClient;
    private final WeatherMapper weatherMapper;
    private final EventMapper eventMapper;

    @Override
    public WeatherResponseDto getCurrentWeather(Location location) {
        Weather weather = weatherClient.getCurrentWeather(location);
        return weatherMapper.mapToWeatherResponseDto(weather);
    }

    @Override
    public EventFullDto validateWeatherForEvent(Event event, Long confirmedRequests, Long views) {
        WeatherResponseDto weather;
        if (event.getEventDate().minusHours(24).isBefore(LocalDateTime.now()) && event.getState().equals(EventState.PUBLISHED)) {
            weather = getCurrentWeather(event.getLocation());
            return eventMapper.mapToEventFullWeatherDto(event, confirmedRequests, views, weather);
        }
        return eventMapper.mapToEventFullDto(event, confirmedRequests, views);
    }
}
