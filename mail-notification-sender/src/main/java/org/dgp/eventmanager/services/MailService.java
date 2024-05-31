package org.dgp.eventmanager.services;

public interface MailService {
    void send(String to, String subject, String text);
}
