package ru.practicum.ewm.dto.weather;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.model.weather.GeoObject;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponseDto {
    private Integer temp;
    private Integer feelsLike;
    private String condition;
    private Integer cloudness;
    private Integer windSpeed;
    private String windDir;
    private Integer pressureMm;
    private Integer humidity;
    private String daytime;
    private String season;
    private GeoObject geoObject;
}
