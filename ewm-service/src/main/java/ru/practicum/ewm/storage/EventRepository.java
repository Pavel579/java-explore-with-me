package ru.practicum.ewm.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.RequestState;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCategoryId(Long catId);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    List<Event> findAllByInitiatorId(Long userId);

    @Query("select e from Event e where e.id = ?2 and e.initiator.id = ?1")
    Event findAllByInitiatorIdAndId(Long userId, Long eventId);

    List<Event> findAllByIdIn(List<Long> events);

    List<Event> findAll(Specification<Event> specification, Pageable pageable);

    @Query("select e.id, (select count(r) from Event ev left join Request r on ev.id = r.event.id where r.status = ?1 and ev.id in ?2) from Event e where e.id in ?2")
    List<Long[]> countAllConfirmedRequests(RequestState name, List<Long> eventsIdsList);

    /*select ev.event_id, (select count(pr.*)" +
            "from events e " +
            "left join participation_requests pr on e.event_id = pr.event_id " +
            "where pr.status = :status " +
            "and e.event_id in :eventIds) " +
            "from events ev " +
            "where ev.event_id in :eventIds  " +
            "group by ev.event_id*/
}
