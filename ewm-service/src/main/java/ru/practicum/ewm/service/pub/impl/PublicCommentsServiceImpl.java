package ru.practicum.ewm.service.pub.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.service.pub.PublicCommentsService;
import ru.practicum.ewm.storage.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCommentsServiceImpl implements PublicCommentsService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> getAllForEvent(Long eventId, PageRequest pageRequest) {
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageRequest);
        return commentMapper.mapToListCommentDto(comments);
    }
}
