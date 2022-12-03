package ru.practicum.ewm.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.utils.Create;
import ru.practicum.ewm.utils.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @NotNull(groups = Update.class)
    private Long id;
    @NotBlank(groups = {Create.class, Update.class})
    private String name;
}
