package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.AdminUpdateEventRequestDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.service.admin.AdminEventsService;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@Validated
@Slf4j
public class AdminEventsController {
    private final AdminEventsService adminEventsService;

    @GetMapping()
    public List<EventFullDto> getByParams(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @FutureOrPresent @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);

        List<EventState> eventStates = null;
        if (states != null) {
            eventStates = states.stream().map(EventState::valueOf).collect(Collectors.toList());
        }
        log.debug("get events contr");
        return adminEventsService.getByParams(users, eventStates, categories, rangeStart, rangeEnd, pageRequest);
    }

    @PutMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody AdminUpdateEventRequestDto adminUpdateEventRequestDto) {
        return adminEventsService.update(eventId, adminUpdateEventRequestDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publish(@PathVariable Long eventId) {
        return adminEventsService.publish(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto reject(@PathVariable Long eventId) {
        return adminEventsService.reject(eventId);
    }
}
