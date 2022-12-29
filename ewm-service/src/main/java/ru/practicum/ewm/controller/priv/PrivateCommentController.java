package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.service.priv.PrivateCommentService;
import ru.practicum.ewm.utils.Create;
import ru.practicum.ewm.utils.Update;

@RestController
@RequestMapping("/users/{userId}/comments")
@Validated
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {
    private final PrivateCommentService privateCommentService;

    @PostMapping
    public CommentDto create(@PathVariable Long userId,
                             @RequestBody @Validated(Create.class) CommentDto commentDto,
                             @RequestParam Long eventId) {
        log.debug("Private comments controller create");
        return privateCommentService.create(userId, eventId, commentDto);
    }

    @PatchMapping
    public CommentDto update(@PathVariable Long userId,
                             @RequestBody @Validated(Update.class) CommentDto commentDto,
                             @RequestParam Long eventId) {
        log.debug("Private comments controller update");
        return privateCommentService.update(userId, eventId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long userId, @PathVariable Long commentId) {
        log.debug("Private comments controller delete");
        privateCommentService.delete(userId, commentId);
    }

}
