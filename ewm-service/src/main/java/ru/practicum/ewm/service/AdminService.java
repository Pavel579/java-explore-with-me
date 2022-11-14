package ru.practicum.ewm.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.user.UserDto;

import java.util.List;

public interface AdminService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids, PageRequest pageRequest);

    void deleteUser(Long userId);

    UserDto getUserById(Long userId);

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    EventFullDto publishEvent(Long eventId);
}
