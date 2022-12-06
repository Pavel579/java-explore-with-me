package ru.practicum.ewm.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.RequestState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCategoryId(Long catId);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("select e from Event e where e.id = ?2 and e.initiator.id = ?1")
    Event findAllByInitiatorIdAndId(Long userId, Long eventId);

    Set<Event> findAllByIdIn(List<Long> events);

    List<Event> findAll(Specification<Event> specification, Pageable pageable);

    @Query("select e.id, (select count(r) from Request r where r.status = ?1 and r.event.id = e.id) from Event e where e.id in ?2 group by e.id")
    List<Long[]> countAllConfirmedRequests(RequestState name, List<Long> eventsIdsList);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    @Query(value = "select * from events e join compilation_events ce on e.id = ce.event_id where ce.compilation_id in (:ids)", nativeQuery = true)
    List<Event> findAllByCompilations(List<Long> ids);
}
