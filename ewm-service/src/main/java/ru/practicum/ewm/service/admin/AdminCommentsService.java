package ru.practicum.ewm.service.admin;

public interface AdminCommentsService {
    void delete(Long commentId);

    void deleteByUser(Long userId);
}
