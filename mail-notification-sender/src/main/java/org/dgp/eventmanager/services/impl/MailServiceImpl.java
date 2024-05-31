package org.dgp.eventmanager.services.impl;

import lombok.RequiredArgsConstructor;
import org.dgp.eventmanager.services.MailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(name = "application.mock-mail-service-enabled", havingValue = "false", matchIfMissing = true)
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    @Override
    public void send(String to, String subject, String text) {
        var message = new SimpleMailMessage();

        message.setFrom("dimgeo@yandex.ru");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
