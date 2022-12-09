package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.service.admin.AdminUsersService;
import ru.practicum.ewm.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@Validated
@Slf4j
public class AdminUsersController {
    private final AdminUsersService adminUsersService;

    @PostMapping()
    public UserDto create(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.debug("Create user contr");
        return adminUsersService.create(userDto);
    }

    @GetMapping()
    public List<UserDto> getAll(
            @RequestParam(name = "ids", required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Admin users controller get all");
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return adminUsersService.getAll(ids, pageRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.debug("Admin users controller delete by id");
        adminUsersService.deleteById(userId);
    }
}
