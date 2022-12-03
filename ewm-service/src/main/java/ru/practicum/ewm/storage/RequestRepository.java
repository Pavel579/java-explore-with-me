package ru.practicum.ewm.storage;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestState;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Request findByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAll(Specification<Event> specification);

    List<Request> findAllByEventIdAndStatus(Long eventId, RequestState pending);


    @Query("SELECT COUNT (r.event) from Request r " +
            "WHERE r.event.id = ?1 AND r.status = ?2")
    Long findConfirmedRequests(Long eventId, RequestState confirmed);

    @Query("select r from Request r join Event e on e.id=r.event.id where e.initiator.id = ?1 and r.event.id = ?2")
    List<Request> findAllByRequesterIdAndEventId(Long userId, Long eventId);

    @Query("SELECT r from Request r where r.id = ?1 and r.requester.id = ?2")
    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);
}
