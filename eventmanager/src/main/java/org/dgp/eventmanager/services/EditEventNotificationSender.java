package org.dgp.eventmanager.services;

import org.dgp.eventmanager.dto.EventDto;

public interface EditEventNotificationSender {
    void send(EventDto event);
}
