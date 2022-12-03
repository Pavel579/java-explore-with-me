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

    @Query("select e.id, (select count(r) from Event ev left join Request r on ev.id = r.event.id where r.status = ?1 and ev.id in ?2) from Event e where e.id in ?2")
    List<Long[]> countAllConfirmedRequests(RequestState name, List<Long> eventsIdsList);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

}
