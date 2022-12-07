package ru.practicum.ewm.service.weather;

import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.dto.weather.WeatherResponseDto;

public interface WeatherService {
    WeatherResponseDto getCurrentWeather(Location location);
}
