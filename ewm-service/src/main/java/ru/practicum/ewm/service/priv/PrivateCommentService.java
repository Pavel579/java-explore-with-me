package ru.practicum.ewm.service.priv;

import ru.practicum.ewm.dto.comment.CommentDto;

public interface PrivateCommentService {
    CommentDto create(Long userId, Long eventId, CommentDto commentDto);

    CommentDto update(Long userId, Long eventId, CommentDto commentDto);

    void delete(Long userId, Long commentId);
}
