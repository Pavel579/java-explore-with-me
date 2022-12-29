package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.admin.AdminCompilationsService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
@Slf4j
public class AdminCompilationsController {
    private final AdminCompilationsService adminCompilationsService;

    @PostMapping()
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.debug("Admin compilations controller create");
        return adminCompilationsService.create(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteById(@PathVariable Long compId) {
        log.debug("Admin compilations controller delete by id");
        adminCompilationsService.deleteById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.debug("Admin compilations controller delete event from compilation");
        adminCompilationsService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.debug("Admin compilation controller add event to compilation");
        adminCompilationsService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void removePinned(@PathVariable Long compId) {
        log.debug("Admin compilation controller remove pinned");
        adminCompilationsService.removePinned(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void setPinned(@PathVariable Long compId) {
        log.debug("Admin compilations controller set pinned");
        adminCompilationsService.setPinned(compId);
    }
}
