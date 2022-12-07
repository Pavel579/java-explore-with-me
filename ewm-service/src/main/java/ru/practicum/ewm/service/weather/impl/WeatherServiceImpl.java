package ru.practicum.ewm.service.weather.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.WeatherClient;
import ru.practicum.ewm.mapper.WeatherMapper;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.weather.Weather;
import ru.practicum.ewm.dto.weather.WeatherResponseDto;
import ru.practicum.ewm.service.weather.WeatherService;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WeatherClient weatherClient;
    private final WeatherMapper weatherMapper;

    @Override
    public WeatherResponseDto getCurrentWeather(Location location) {
        Weather weather = weatherClient.getCurrentWeather(location);
        return weatherMapper.mapToWeatherResponseDto(weather);
    }
}
