package org.dgp.eventmanager.services.impl;

import org.dgp.eventmanager.services.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateTimeProviderImpl implements DateTimeProvider {

    @Override
    public LocalDate today() {
        return LocalDate.of(2025, 4, 9);
    }
}
