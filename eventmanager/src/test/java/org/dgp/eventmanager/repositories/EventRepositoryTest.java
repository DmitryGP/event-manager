package org.dgp.eventmanager.repositories;

import org.dgp.eventmanager.JpaRepositoryBaseTest;
import org.dgp.eventmanager.models.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class EventRepositoryTest extends JpaRepositoryBaseTest {

    @Autowired
    private EventRepository repository;

    @Test
    @Sql(scripts = {"/scripts/drop_data.sql", "/scripts/test_data.sql"})
    void findAllByParticipant() {
        var actual = repository.findAllByParticipant(2);

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.stream()
                .map(Event::getId)
                .toList())
                .containsExactly(1L, 2L);
    }

    @Test
    @Sql(scripts = {"/scripts/drop_data.sql", "/scripts/test_data.sql"})
    void findAllByOwnerId() {
        var actual = repository.findAllByOwnerId(1);

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.stream()
                .map(Event::getId).toList())
                .containsExactly(1L, 3L);
    }

    @Test
    @Sql(scripts = {"/scripts/drop_data.sql", "/scripts/test_data.sql"})
    void findAllByStartDateTimeMoreThan() {
        var actual = repository.findByStartDateGreaterThan(LocalDate.of(2024, 4, 1));

        assertThat(actual.size()).isEqualTo(2);
    }
}
