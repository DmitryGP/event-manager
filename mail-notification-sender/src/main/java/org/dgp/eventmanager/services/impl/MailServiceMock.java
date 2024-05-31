package org.dgp.eventmanager.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.dgp.eventmanager.services.MailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "application.mock-mail-service-enabled", havingValue = "true")
public class MailServiceMock implements MailService {
    @Override
    public void send(String to, String subject, String text) {
        log.atInfo()
                .setMessage("Mail to [{}] with subject [{}] was sent.\n Text: [{}]")
                .addArgument(to)
                .addArgument(subject)
                .addArgument(text)
                .log();
    }
}
