package org.dgp.eventmanager.repositories;

import org.assertj.core.api.Assertions;
import org.dgp.eventmanager.JpaRepositoryBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerRepositoryTest extends JpaRepositoryBaseTest {

    @Autowired
    private OwnerRepository repository;

    @Test
    @Sql(scripts = {"/scripts/drop_data.sql", "/scripts/test_data.sql"})
    void findByEmail() {
        var actual = repository.findByEmail("aaa@host.org");

        assertThat(actual).isPresent();
    }
}
