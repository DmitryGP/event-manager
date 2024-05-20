package org.dgp.eventmanager.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.dgp.eventmanager.dto.CreateEventDto;
import org.dgp.eventmanager.dto.EditEventDto;
import org.dgp.eventmanager.dto.EventDto;
import org.dgp.eventmanager.exceptions.LogicException;
import org.dgp.eventmanager.exceptions.NotFoundException;
import org.dgp.eventmanager.mappers.EventMapper;
import org.dgp.eventmanager.repositories.EventRepository;
import org.dgp.eventmanager.repositories.ParticipantRepository;
import org.dgp.eventmanager.services.EditEventNotificationSender;
import org.dgp.eventmanager.services.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repository;

    private final EventMapper mapper;

    private final ParticipantRepository participantRepository;

    private final EditEventNotificationSender editEventNotificationSender;
    @Override
    @Transactional
    public EventDto create(CreateEventDto event) {

        var newEvent = mapper.map(event);

        var savedEvent = repository.save(newEvent);

        return mapper.map(savedEvent);
    }

    @Override
    public List<EventDto> findOwnedEvents(long ownerId) {

        var events = repository.findAllByOwnerId(ownerId);

        return events.stream().map(mapper::map).toList();
    }

    @Override
    public EventDto find(long eventId) {
        var event = repository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id = %s is not found".formatted(eventId)));

        return mapper.map(event);
    }

    @Override
    public List<EventDto> findParticipatedEvents(long participantId) {

        var events = repository.findAllByParticipant(participantId);

        return events.stream().map(mapper::map).toList();
    }



    @Override
    @Transactional
    public EventDto addParticipant(long eventId, long participantId) {

        var event = repository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("Event with id = %s is not found".formatted(eventId)));

        if(event.getMaxParticipantsCount() <= event.getParticipants().size()) {
            throw new LogicException("Maximum of participant count for event [%s] has been reached."
                    .formatted(event));
        }

        var participant = participantRepository.findById(participantId)
                .orElseThrow(() ->
                        new NotFoundException("Participant with id = %s is not found".formatted(participantId)));

        event.getParticipants().add(participant);

        var savedEvent = repository.save(event);

        return mapper.map(savedEvent);
    }

    @Override
    @Transactional
    public EventDto editEvent(EditEventDto editEvent) {

        var event = repository.findById(editEvent.getId())
                .orElseThrow(() ->
                        new NotFoundException("Event with id = %s is not found".formatted(editEvent.getId())));

        if (editEvent.getName() != null &&
                Strings.isNotEmpty(editEvent.getName())) {
            event.setName(editEvent.getName());
        }

        if (editEvent.getDescription() != null &&
                Strings.isNotEmpty(editEvent.getDescription())) {
            event.setDescription(editEvent.getDescription());
        }

        if(editEvent.getStartDateTime() != null) {
            if(ZonedDateTime.now().isAfter(editEvent.getStartDateTime())) {
                throw new LogicException("Start date is in the past.");
            }

            event.setStartDateTime(editEvent.getStartDateTime());
        }

        if(editEvent.getEndDateTime() != null) {
            if(ZonedDateTime.now().isAfter(editEvent.getEndDateTime())) {
                throw new LogicException("End date is in the past.");
            }

            event.setEndDateTime(editEvent.getEndDateTime());
        }

        var savedEvent = mapper.map(repository.save(event));

        editEventNotificationSender.send(savedEvent);

        return savedEvent;
    }

    @Override
    public List<EventDto> findAllActual() {
        var events = repository.findByStartDateTimeGreaterThan(ZonedDateTime.now());

        return events.stream().map(mapper::map).toList();
    }
}
