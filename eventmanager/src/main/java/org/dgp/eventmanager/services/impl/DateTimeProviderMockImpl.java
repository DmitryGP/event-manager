package org.dgp.eventmanager.services.impl;

import org.dgp.eventmanager.services.DateTimeProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(name = "application.date-time-mock", havingValue = "true")
public class DateTimeProviderMockImpl implements DateTimeProvider {

    @Override
    public LocalDate today() {
        return LocalDate.of(2025, 4, 9);
    }
}
