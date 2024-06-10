package org.dgp.eventmanager.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dgp.eventmanager.dto.EventDto;
import org.dgp.eventmanager.notifications.EditEventMessage;
import org.dgp.eventmanager.services.EditEventNotificationSender;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
//@Service
@RequiredArgsConstructor
public class EditEventNotificationSenderKafkaImpl implements EditEventNotificationSender {

    private final String topicName;

    private final KafkaTemplate<String, EditEventMessage> kafkaTemplate;

    @Override
    public void send(EventDto event) {
        log.atInfo()
                .setMessage("Sending edit event [id = {}] notification.")
                .addArgument(event.getId())
                .log();

        kafkaTemplate.send(topicName, new EditEventMessage(event))
                .whenComplete((result, e) -> {
                    if (e == null) {
                        log.atInfo()
                                .setMessage("Edit notification for event id = {} was sent.")
                                .addArgument(event.getId())
                                .log();
                    } else {
                        log.atError()
                                .setMessage("Exception appeared: {}")
                                .addArgument(e.getMessage())
                                .setCause(e)
                                .log();

                    }
                        });
    }
}
