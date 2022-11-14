package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequestDto {
    private Long id;
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Long eventId;
    private Boolean paid;
    private Integer participantLimit;
    private String title;
}
