package org.dgp.eventmanager.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dgp.eventmanager.dto.EventDto;
import org.dgp.eventmanager.external.NearestEventsNotificationMessage;
import org.dgp.eventmanager.services.NearestEventsNotificationSender;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NearestEventsNotificationSenderKafkaImpl implements NearestEventsNotificationSender {

    private final String topicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(List<EventDto> events) {
        log.atInfo()
                .setMessage("Sending nearest events notification.")
                .log();

        kafkaTemplate.send(topicName, new NearestEventsNotificationMessage(events))
                .whenComplete((result, e) -> {
                    if(e == null) {
                        log.atInfo()
                                .setMessage("Nearest events notification was sent.")
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
