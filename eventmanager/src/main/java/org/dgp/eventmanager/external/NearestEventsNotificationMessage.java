package org.dgp.eventmanager.external;

import lombok.Getter;
import lombok.Setter;
import org.dgp.eventmanager.dto.EventDto;

import java.util.List;

@Getter
@Setter
public class NearestEventsNotificationMessage {

    private List<EventDto> nearestEvents;

    public NearestEventsNotificationMessage(List<EventDto> events) {
        this.nearestEvents = List.copyOf(events);
    }
}
