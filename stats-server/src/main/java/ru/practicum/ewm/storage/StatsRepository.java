package ru.practicum.ewm.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.ewm.dto.ViewStatsDto(e.app, e.uri, COUNT (e.ip)) from EndpointHit e " +
            "WHERE e.timestamp> ?1 AND e.timestamp< ?2 GROUP BY e.app, e.uri")
    List<ViewStatsDto> findAll(LocalDateTime startFrom, LocalDateTime endBefore, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.dto.ViewStatsDto(e.app, e.uri, COUNT (DISTINCT e.ip)) from EndpointHit e " +
            "WHERE e.timestamp> ?1 AND e.timestamp< ?2 GROUP BY e.app, e.uri")
    List<ViewStatsDto> findAllUnique(LocalDateTime startFrom, LocalDateTime endBefore, List<String> uris);
}
