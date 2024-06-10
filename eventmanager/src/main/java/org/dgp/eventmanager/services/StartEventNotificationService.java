package org.dgp.eventmanager.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.dgp.eventmanager.mappers.EventMapper;
import org.dgp.eventmanager.repositories.EventRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StartEventNotificationService {

    private final EventRepository eventRepository;

    private final EventMapper mapper;

    private final NearestEventsNotificationSender nearestEventsNotificationSender;

    private final DateTimeProvider dateTimeProvider;

    @Scheduled(cron = "${application.scheduler.cron}")
    @SchedulerLock(name = "sendingNearestEventsNotificationTask")
    @Async
    @Transactional
    public void processStartEventNotification() {
        log.atInfo()
                .setMessage("Nearest events process started...")
                .log();

        var nearestEvents = eventRepository.
                findByStartDate(dateTimeProvider.today().plusDays(1))
                .stream()
                .map(mapper::map)
                .toList();

        if (nearestEvents.size() > 0) {
            nearestEventsNotificationSender.send(nearestEvents);
        }
    }
}
