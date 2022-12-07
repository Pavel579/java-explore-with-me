package ru.practicum.ewm.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Fact {
    private Integer temp;
    @JsonProperty("feels_like")
    private Integer feelsLike;
    private String condition;
    private Integer cloudness;
    @JsonProperty("wind_speed")
    private Integer windSpeed;
    @JsonProperty("wind_dir")
    private String windDir;
    @JsonProperty("pressure_mm")
    private Integer pressureMm;
    private Integer humidity;
    private String daytime;
    private String season;
}
