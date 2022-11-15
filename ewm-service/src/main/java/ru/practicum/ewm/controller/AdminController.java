package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.event.AdminUpdateEventRequestDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.service.AdminService;
import ru.practicum.ewm.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;
    /*
    EVENTS
     */
    @GetMapping("/events")
    public List<EventFullDto> getEventsByParams(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        List<EventState> eventStates = new ArrayList<>();
        for (String s : states) {
            eventStates.add(EventState.valueOf(s));
        }
        return adminService.getEventsByParams(users, eventStates, categories, rangeStart, rangeEnd, pageRequest);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody AdminUpdateEventRequestDto adminUpdateEventRequestDto) {
        return adminService.updateEvent(eventId, adminUpdateEventRequestDto);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        return adminService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        return adminService.rejectEvent(eventId);
    }

    /*
    CATEGORIES
     */
    @PostMapping("/categories")
    public CategoryDto createCategory(@Validated @RequestBody CategoryDto categoryDto) {
        return adminService.createCategory(categoryDto);
    }

    @PatchMapping("/categories")
    public CategoryDto updateCategory(@Validated @RequestBody CategoryDto categoryDto) {
        return adminService.updateCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        adminService.deleteCategory(catId);
    }

    /*
    USERS
     */
    @PostMapping("/users")
    public UserDto createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("vcxbcxvb");
        return adminService.createUser(userDto);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(
            @RequestParam(name = "ids", required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return adminService.getUsers(ids, pageRequest);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
    }

    /*
    COMPILATIONS
     */
    @PostMapping("/compilations")
    public CompilationDto createCompilation(@Validated @RequestBody NewCompilationDto newCompilationDto) {
        return adminService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilationById(@PathVariable Long compId) {
        adminService.deleteCompilationById(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        adminService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        adminService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void removePinnedCompilation(@PathVariable Long compId) {
        adminService.removePinnedCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void setPinnedCompilation(@PathVariable Long compId) {
        adminService.setPinnedCompilation(compId);
    }


}
