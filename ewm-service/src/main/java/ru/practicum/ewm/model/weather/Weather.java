package ru.practicum.ewm.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    private Integer now;
    @JsonProperty("now_dt")
    private String nowDt;
    @JsonProperty("geo_object")
    private GeoObject geoObject;
    private Fact fact;
    private List<Object> forcasts;
}
