package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventFullWeatherDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventRequestDto;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.service.priv.PrivateEventsService;
import ru.practicum.ewm.utils.Create;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@RequiredArgsConstructor
@Slf4j
public class PrivateEventsController {
    private final PrivateEventsService privateEventsService;

    @PostMapping()
    public EventFullDto create(@Validated(Create.class) @RequestBody NewEventDto newEventDto,
                               @PathVariable Long userId) {
        log.debug("Create event private contr");
        return privateEventsService.create(userId, newEventDto);
    }

    @PatchMapping()
    public EventFullDto update(@Valid @RequestBody UpdateEventRequestDto updateEventDto,
                               @PathVariable Long userId) {
        return privateEventsService.update(updateEventDto, userId);
    }

    @GetMapping()
    public List<EventShortDto> getByUser(
            @PathVariable Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        return privateEventsService.getByUser(userId, pageRequest);
    }

    @GetMapping("/{eventId}")
    public EventFullWeatherDto getByUserIdAndEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("get event by id contr");
        return privateEventsService.getByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancel(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateEventsService.cancel(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsOfUser(@PathVariable Long userId,
                                                                        @PathVariable Long eventId) {
        return privateEventsService.getParticipationRequestsOfUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                                  @PathVariable Long reqId) {
        return privateEventsService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId, @PathVariable Long reqId) {
        return privateEventsService.rejectRequest(userId, eventId, reqId);
    }
}
