package ru.practicum.ewm.service.admin.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.service.admin.AdminCommentsService;
import ru.practicum.ewm.storage.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminCommentsServiceImpl implements AdminCommentsService {
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteByUser(Long userId) {
        List<Comment> comments = commentRepository.findAllByAuthorId(userId);
        commentRepository.deleteAll(comments);
    }
}
