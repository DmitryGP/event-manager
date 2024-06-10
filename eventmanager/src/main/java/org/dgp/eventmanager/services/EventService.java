package org.dgp.eventmanager.services;

import org.dgp.eventmanager.dto.CreateEventDto;
import org.dgp.eventmanager.dto.EditEventDto;
import org.dgp.eventmanager.dto.EventDto;

import java.util.List;

public interface EventService {
    EventDto create(CreateEventDto event);

    List<EventDto> findOwnedEvents(long ownerId);

    EventDto find(long eventId);

    List<EventDto> findParticipatedEvents(long participantId);

    EventDto addParticipant(long eventId, long participantId);

    EventDto editEvent(EditEventDto event);

    List<EventDto> findAllActual();
}
