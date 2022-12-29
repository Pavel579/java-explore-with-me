package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public Comment mapToComment(CommentDto commentDto, Event event, User user) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                event,
                user,
                LocalDateTime.now());
    }

    public CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public List<CommentDto> mapToListCommentDto(List<Comment> list) {
        return list.stream().map(this::mapToCommentDto).collect(Collectors.toList());
    }
}
