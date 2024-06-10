package org.dgp.eventmanager.services;

import org.dgp.eventmanager.dto.EventDto;

import java.util.List;

public interface MailSender {
    void sendEditEventNotification(EventDto event);

    void sendNearestEventNotifications(List<EventDto> nearestEvents);
}
