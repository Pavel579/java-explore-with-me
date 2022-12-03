package ru.practicum.ewm.service.admin;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.user.UserDto;

import java.util.List;

public interface AdminUsersService {
    UserDto create(UserDto userDto);

    List<UserDto> getAll(List<Long> ids, PageRequest pageRequest);

    void deleteById(Long userId);

    UserDto getById(Long userId);
}
