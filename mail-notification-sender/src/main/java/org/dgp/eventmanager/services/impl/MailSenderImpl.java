package org.dgp.eventmanager.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.dgp.eventmanager.dto.EventDto;
import org.dgp.eventmanager.services.MailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MailSenderImpl implements MailSender {
    @Override
    public void sendEditEventNotification(EventDto event) {
        log.atInfo()
                .setMessage("Sending edit event notification for id = {}")
                .addArgument(event.getId())
                .log();
    }

    @Override
    public void sendNearestEventNotifications(List<EventDto> nearestEvents) {
        log.atInfo()
                .setMessage("Sending nearest event notifications for id(s) = {}")
                .addArgument(nearestEvents.stream()
                        .map(EventDto::getId)
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")))
                .log();
    }
}
