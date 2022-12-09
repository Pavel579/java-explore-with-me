package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.service.priv.PrivateRequestsService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestsController {
    private final PrivateRequestsService privateRequestsService;

    @PostMapping()
    public ParticipationRequestDto create(@PathVariable Long userId, @RequestParam Long eventId) {
        log.debug("Private request controller create");
        return privateRequestsService.create(userId, eventId);
    }

    @GetMapping()
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        log.debug("Private request controller get user request");
        return privateRequestsService.getUserRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        log.debug("Private requests controller cancel");
        return privateRequestsService.cancel(userId, requestId);
    }
}
