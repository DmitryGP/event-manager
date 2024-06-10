package org.dgp.eventmanager.notifications;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dgp.eventmanager.dto.EventDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NearestEventsNotificationMessage {

    private List<EventDto> nearestEvents;

    public NearestEventsNotificationMessage(List<EventDto> events) {
        this.nearestEvents = List.copyOf(events);
    }
}
