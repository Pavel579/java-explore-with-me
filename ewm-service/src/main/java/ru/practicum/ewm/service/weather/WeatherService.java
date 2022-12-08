package ru.practicum.ewm.service.weather;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.dto.weather.WeatherResponseDto;

public interface WeatherService {
    WeatherResponseDto getCurrentWeather(Location location);

    EventFullDto validateWeatherForEvent(Event event, Long confirmedRequests, Long views);
}
