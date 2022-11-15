package ru.practicum.ewm.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCategoryId(Long catId);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("select e from Event e where e.id = ?2 and e.initiator.id = ?1")
    Event findAllByInitiatorIdAndId(Long userId, Long eventId);

    List<Event> findAllByIdIn(List<Long> events);

    @Query("select e from Event e where (e.id is null or e.initiator.id = :users) and (e.state is null or e.state = :states) and " +
            "(e.category.id is null or e.category.id = :categories) and (e.eventDate is null or e.eventDate > :rangeStart) and " +
            "(e.eventDate is null or e.eventDate < :rangeEnd)")
    List<Event> findAllEventsByParams(List<Long> users, List<EventState> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    //List<Event> findAll(CriteriaBuilder criteriaBuilder, Sort sort1, PageRequest pageRequest);
}
