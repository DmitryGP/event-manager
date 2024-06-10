package org.dgp.eventmanager.initialization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.stream.Stream;

@Slf4j
public class PostgresContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:13")
                    .withDatabaseName("event_manager")
                    .withUsername("usr")
                    .withPassword("pwd");
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Startables.deepStart(Stream.of(postgresContainer)).join();

        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.flyway.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.flyway.password", postgresContainer.getPassword());
        System.setProperty("spring.flyway.username", postgresContainer.getUsername());

        log.atInfo().setMessage("Data base initialization is completed.").log();
    }
}
