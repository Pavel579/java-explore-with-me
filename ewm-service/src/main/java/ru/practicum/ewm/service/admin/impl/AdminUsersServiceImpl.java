package ru.practicum.ewm.service.admin.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.service.admin.AdminUsersService;
import ru.practicum.ewm.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminUsersServiceImpl implements AdminUsersService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        log.debug("create user service");
        User user = userRepository.save(userMapper.mapToUser(userDto));
        return userMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAll(List<Long> ids, PageRequest pageRequest) {
        List<User> users;
        if (ids != null && !ids.isEmpty()) {
            users = userRepository.findAllByIdIn(ids, pageRequest);
        } else {
            users = userRepository.findAll(pageRequest).toList();
        }
        return userMapper.mapToListUserDto(users);
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getById(Long userId) {
        return userMapper.mapToUserDto(userRepository
                .findById(userId).orElseThrow(() -> new NotFoundException("user not fond")));
    }
}
