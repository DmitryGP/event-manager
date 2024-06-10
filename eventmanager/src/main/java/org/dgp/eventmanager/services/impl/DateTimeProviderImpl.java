package org.dgp.eventmanager.services.impl;

import org.dgp.eventmanager.services.DateTimeProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@ConditionalOnProperty(name = "application.date-time-mock", havingValue = "false", matchIfMissing = true)
public class DateTimeProviderImpl implements DateTimeProvider {

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }
}
