package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.model.weather.Weather;
import ru.practicum.ewm.dto.weather.WeatherResponseDto;

@Component
public class WeatherMapper {
    public WeatherResponseDto mapToWeatherResponseDto(Weather weather) {
        return new WeatherResponseDto(
                weather.getFact().getTemp(),
                weather.getFact().getFeelsLike(),
                weather.getFact().getCondition(),
                weather.getFact().getCloudness(),
                weather.getFact().getWindSpeed(),
                weather.getFact().getWindDir(),
                weather.getFact().getPressureMm(),
                weather.getFact().getHumidity(),
                weather.getFact().getDaytime(),
                weather.getFact().getSeason(),
                weather.getGeoObject()
        );
    }
}
