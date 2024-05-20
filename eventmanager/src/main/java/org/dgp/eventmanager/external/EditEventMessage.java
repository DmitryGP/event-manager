package org.dgp.eventmanager.external;

import lombok.Getter;
import lombok.Setter;
import org.dgp.eventmanager.dto.EventDto;

@Getter
@Setter
public class EditEventMessage {

    private EventDto event;

    public EditEventMessage(EventDto event) {
        this.event = event;
    }

}
