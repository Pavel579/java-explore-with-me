package ru.practicum.ewm.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.utils.Create;
import ru.practicum.ewm.utils.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @NotNull(groups = Update.class)
    private Long id;
    @NotBlank(groups = {Create.class, Update.class})
    private String text;
    private String authorName;
    private LocalDateTime created;
}
