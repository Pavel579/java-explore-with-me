package ru.practicum.ewm.service.priv.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.exceptions.ForbiddenException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.exceptions.ValidationException;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.service.priv.PrivateCommentService;
import ru.practicum.ewm.storage.CommentRepository;
import ru.practicum.ewm.storage.EventRepository;
import ru.practicum.ewm.storage.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto create(Long userId, Long eventId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.getState().equals(EventState.PUBLISHED)) {
            Comment comment = commentRepository.save(commentMapper.mapToComment(commentDto, event, user));
            return commentMapper.mapToCommentDto(comment);
        } else {
            throw new ForbiddenException("Can't comment not published events");
        }

    }

    @Override
    @Transactional
    public CommentDto update(Long userId, Long eventId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (comment.getAuthor().getId().equals(userId) && comment.getEvent().getId().equals(eventId)) {
            commentRepository.save(commentMapper.mapToComment(commentDto, event, user));
        } else {
            throw new ValidationException("Wrong user or event");
        }
        return commentMapper.mapToCommentDto(comment);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long commentId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (userId.equals(comment.getAuthor().getId()) || userId.equals(comment.getEvent().getInitiator().getId())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new ForbiddenException("No authorisation to delete comment");
        }
    }
}
