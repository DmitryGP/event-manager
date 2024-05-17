package org.dgp.eventmanager.repositories;

import org.dgp.eventmanager.JpaRepositoryBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

public class ParticipantsRepositoryTest extends JpaRepositoryBaseTest {
    @Autowired
    private ParticipantRepository repository;

    @Test
    @Sql(scripts = {"/scripts/drop_data.sql", "/scripts/test_data.sql"})
    void findByEmail() {
        var actual = repository.findByEmail("eee@host.org");

        assertThat(actual).isPresent();
        assertThat(actual.get().getId()).isEqualTo(3);
    }

    @Test
    @Sql(scripts = {"/scripts/drop_data.sql", "/scripts/test_data.sql"})
    void findByEmailShouldReturnNothingWhenEmailIsNotExisted() {
        var actual = repository.findByEmail("noname@host.org");

        assertThat(actual).isEmpty();
    }
}
