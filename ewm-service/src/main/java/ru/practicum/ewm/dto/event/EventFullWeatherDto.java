package ru.practicum.ewm.dto.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.dto.weather.WeatherResponseDto;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EventFullWeatherDto extends EventFullDto {
    private WeatherResponseDto weather;

    public EventFullWeatherDto(Long id, String annotation, CategoryDto category, Long confirmedRequests, LocalDateTime createdOn, String description, LocalDateTime eventDate, UserShortDto initiator, Location location, boolean paid, int participantLimit, LocalDateTime publishedOn, boolean requestModeration, EventState state, String title, Long views, WeatherResponseDto weather) {
        super(id, annotation, category, confirmedRequests, createdOn, description, eventDate, initiator, location, paid, participantLimit, publishedOn, requestModeration, state, title, views);
        this.weather = weather;
    }
}
