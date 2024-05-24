package org.dgp.eventmanager.configuration;

import org.dgp.eventmanager.notifications.EditEventMessage;
import org.dgp.eventmanager.notifications.NearestEventsNotificationMessage;
import org.dgp.eventmanager.services.MailSender;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

@KafkaListener(topics = "${application.kafka.edit-event-notification-topic}",
        containerFactory = "listenerContainerFactory")
public class KafkaClient {

    private final MailSender mailSender;

    public KafkaClient(MailSender mailSender) {
        this.mailSender = mailSender;
    }


    @KafkaHandler
    public void listenEditEventNotification(@Payload EditEventMessage message) {
        mailSender.sendEditEventNotification(message.getEvent());
    }

    @KafkaHandler
    public void listenNearestEventsNotification(@Payload NearestEventsNotificationMessage message) {
        mailSender.sendNearestEventNotifications(message.getNearestEvents());
    }
}
