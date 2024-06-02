package org.dgp.eventmanager.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dgp.eventmanager.dto.EventDto;
import org.dgp.eventmanager.dto.ParticipantDto;
import org.dgp.eventmanager.services.MailSender;
import org.dgp.eventmanager.services.MailService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final MailService mailService;

    @Override
    public void sendEditEventNotification(EventDto event) {
        log.atInfo()
                .setMessage("Sending edit event notification for id = {}")
                .addArgument(event.getId())
                .log();

        var text = "Hi, Inform you about event changes.\n%s\nStart at: %s %s";
        var subject = "Event changed";

        event.getParticipants()
                .forEach(p -> {
                            sendEditEventMailToParticipant(p, subject,
                                    text.formatted(event.getName(),
                                    event.getStartDate().format(DateTimeFormatter.ISO_DATE),
                                    event.getStartTime().format(DateTimeFormatter.ISO_LOCAL_TIME)));
                        }
                );

        log.atInfo()
                .setMessage("Edit event notification for id = {} was sent")
                .addArgument(event.getId())
                .log();
    }

    @Override
    public void sendNearestEventNotifications(List<EventDto> nearestEvents) {
        var eventIdString = nearestEvents.stream().map(EventDto::getId).map(Object::toString)
                .collect(Collectors.joining(", "));

        log.atInfo()
                .setMessage("Sending nearest event notifications for id(s) = {}")
                .addArgument(eventIdString)
                .log();

        var text = "Hi, Inform you about the nearest event.\n%s\nStart at: %s %s";
        var subject = "Nearest event";

        nearestEvents.forEach(e ->
                e.getParticipants().forEach(p -> {
                            sendNearestEventMailToParticipant(p, subject, text.formatted(e.getName(),
                                    e.getStartDate().format(DateTimeFormatter.ISO_DATE),
                                    e.getStartTime().format(DateTimeFormatter.ISO_LOCAL_TIME)));
                        }
                        ));

        log.atInfo()
                .setMessage("Nearest event notifications for id(s) = {} was sent")
                .addArgument(eventIdString)
                .log();
    }

    private void sendNearestEventMailToParticipant(ParticipantDto participant, String subject, String text) {
        try {
            mailService.send(participant.getEmail(), subject,
                    text);
        } catch (Exception exc) {
            log.atError()
                    .setMessage("Error happened while sending nearest event mail to {}. {}")
                    .addArgument(participant.getEmail())
                    .addArgument(exc.toString())
                    .log();
        }
    }

    private void sendEditEventMailToParticipant(ParticipantDto participant, String subject, String text) {
        try {
            mailService.send(participant.getEmail(), subject,
                    text);
        } catch (Exception exc) {
            log.atError()
                    .setMessage("Error happened while sending change event mail to {}. {}")
                    .addArgument(participant.getEmail())
                    .addArgument(exc.toString())
                    .log();
        }
    }
}
