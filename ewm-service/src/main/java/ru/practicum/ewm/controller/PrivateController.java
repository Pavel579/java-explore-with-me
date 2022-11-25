package ru.practicum.ewm.controller;

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
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventRequestDto;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.service.PrivateService;
import ru.practicum.ewm.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateController {
    private final PrivateService privateService;

    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@Validated(Create.class) @RequestBody NewEventDto newEventDto,
                                    @PathVariable Long userId) {
        log.debug("Create event contr");
        return privateService.createEvent(userId, newEventDto);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateEvent(@Validated @RequestBody UpdateEventRequestDto updateEventDto,
                                    @PathVariable Long userId) {
        return privateService.updateEvent(updateEventDto, userId);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUser(
            @PathVariable Long userId,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        PageRequest pageRequest = null;
        if (from != null && size != null) {
            int page = from / size;
            pageRequest = PageRequest.of(page, size);
        }
        return privateService.getEventsByUser(userId, pageRequest);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUserIdAndEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("get event by id contr");
        return privateService.getEventByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsOfUser(@PathVariable Long userId,
                                                                        @PathVariable Long eventId) {
        return privateService.getParticipationRequestsOfUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                                  @PathVariable Long reqId) {
        return privateService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId, @PathVariable Long reqId) {
        return privateService.rejectRequest(userId, eventId, reqId);
    }


    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return privateService.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        return privateService.getUserRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return privateService.cancelRequest(userId, requestId);
    }
}
