package org.dgp.eventmanager.services;

import org.dgp.eventmanager.dto.EventDto;

import java.util.List;

public interface NearestEventsNotificationSender {
    void send(List<EventDto> events);
}
