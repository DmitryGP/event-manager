package org.dgp.eventmanager.repositories;

import org.dgp.eventmanager.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOwnerId(long ownerId);

    @Query("SELECT e FROM Event e, IN(e.participants) p WHERE p.id = :participantId")
    List<Event> findAllByParticipant(long participantId);

    List<Event> findByStartDateGreaterThan(LocalDate date);

    List<Event> findByStartDate(LocalDate date);
}
