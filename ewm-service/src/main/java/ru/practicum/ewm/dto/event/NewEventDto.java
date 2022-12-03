package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.utils.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    @Size(max = 2000, min = 20)
    private String annotation;
    @NotNull(groups = {Create.class})
    private Long category;
    @NotBlank(groups = {Create.class})
    @Size(max = 7000, min = 20)
    private String description;
    @NotNull(groups = {Create.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;
    @NotNull(groups = {Create.class})
    private Location location;
    private boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank(groups = {Create.class})
    @Size(max = 120, min = 3)
    private String title;
}
