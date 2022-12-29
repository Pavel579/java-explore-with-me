package ru.practicum.ewm.service.pub;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.comment.CommentDto;

import java.util.List;

public interface PublicCommentsService {
    List<CommentDto> getAllForEvent(Long eventId, PageRequest pageRequest);
}
