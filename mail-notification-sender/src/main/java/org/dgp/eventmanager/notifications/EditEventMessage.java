package org.dgp.eventmanager.notifications;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dgp.eventmanager.dto.EventDto;

@Getter
@Setter
@NoArgsConstructor
public class EditEventMessage {

    private EventDto event;

    public EditEventMessage(EventDto event) {
        this.event = event;
    }

}
