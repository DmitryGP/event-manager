package org.dgp.eventmanager.configuration;

import lombok.extern.slf4j.Slf4j;
import org.dgp.eventmanager.notifications.EditEventMessage;
import org.dgp.eventmanager.notifications.NearestEventsNotificationMessage;
import org.dgp.eventmanager.services.MailSender;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class KafkaClient {

    private final MailSender mailSender;

    private final ConcurrentLinkedQueue<EditEventMessage> editEventMessagesQueue;

    private final ConcurrentLinkedQueue<NearestEventsNotificationMessage> nearestEventsNotificationMessagesQueue;

    private final ExecutorService editEventMessagesExecutor;

    private final ExecutorService nearestEventsNotificationMessagesExecutor;

    public KafkaClient(MailSender mailSender) {
        this.mailSender = mailSender;
        this.editEventMessagesQueue = new ConcurrentLinkedQueue<>();
        this.nearestEventsNotificationMessagesQueue = new ConcurrentLinkedQueue<>();

        this.editEventMessagesExecutor = Executors.newSingleThreadExecutor();
        this.nearestEventsNotificationMessagesExecutor = Executors.newSingleThreadExecutor();

        editEventMessagesExecutor.submit(() -> editEventMessagesProcess(mailSender));
        nearestEventsNotificationMessagesExecutor.submit(() -> nearestEventsNotificationMessagesProcess(mailSender));
    }


    @KafkaListener(topics = "${application.kafka.edit-event-notification-topic}",
            groupId = "mail-group-1",
            containerFactory = "editEventMessageListenerContainerFactory")
    public void listenEditEventNotification(@Payload EditEventMessage message) {
        this.editEventMessagesQueue.offer(message);
        log.atInfo()
                .setMessage("Edit Event Message was put into queue. Messages in queue: {}")
                .addArgument(this.editEventMessagesQueue.size())
                .log();
    }

    @KafkaListener(topics = "${application.kafka.nearest-events-notification-topic}",
            groupId = "mail-group-2",
            containerFactory = "nearestEventsNotificationMessageListenerContainerFactory")
    public void listenNearestEventsNotification(@Payload NearestEventsNotificationMessage message) {
        this.nearestEventsNotificationMessagesQueue.offer(message);
    }

    private void editEventMessagesProcess(MailSender mailSender) {
        while (true) {
            var message = editEventMessagesQueue.poll();

            if (message != null) {
                mailSender.sendEditEventNotification(message.getEvent());
            } else {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void nearestEventsNotificationMessagesProcess(MailSender mailSender) {
        while (true) {
            var message = nearestEventsNotificationMessagesQueue.poll();

            if (message != null) {
                mailSender.sendNearestEventNotifications(message.getNearestEvents());
            } else {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
