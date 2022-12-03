package ru.practicum.ewm.service.pub;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.category.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getAll(PageRequest pageRequest);

    CategoryDto getById(Long catId);
}
