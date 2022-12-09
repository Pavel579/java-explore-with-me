package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.admin.AdminCommentsService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
@Slf4j
public class AdminCommentsController {
    private final AdminCommentsService adminCommentsService;

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId) {
        log.debug("Admin comments controller delete by id");
        adminCommentsService.delete(commentId);
    }

    @DeleteMapping
    public void deleteByUser(@RequestParam Long userId) {
        log.debug("Admin comments controller delete by user");
        adminCommentsService.deleteByUser(userId);
    }
}
