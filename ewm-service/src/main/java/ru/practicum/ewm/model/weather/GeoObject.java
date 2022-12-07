package ru.practicum.ewm.model.weather;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GeoObject {
    private String district;
    private WeatherLocation locality;
    private WeatherLocation province;
    private WeatherLocation country;

}
