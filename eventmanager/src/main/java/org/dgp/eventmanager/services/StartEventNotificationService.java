package org.dgp.eventmanager.services;

import lombok.RequiredArgsConstructor;
import org.dgp.eventmanager.mappers.EventMapper;
import org.dgp.eventmanager.repositories.EventRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Service
public class StartEventNotificationService {

    private final EventRepository eventRepository;

    private final EventMapper mapper;

    private final NearestEventsNotificationSender nearestEventsNotificationSender;

    @Scheduled(cron = "${application.scheduler.cron}")
    @Async
    public void processStartEventNotification() {
        var nearestEvents = eventRepository.
                findByStartDateTime(ZonedDateTime.now().minusDays(1))
                .stream()
                .map(mapper::map)
                .toList();

        nearestEventsNotificationSender.send(nearestEvents);
    }
}
