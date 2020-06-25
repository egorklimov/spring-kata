package com.github.egorklimov.springkata.integration.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@RequiredArgsConstructor
class DataSourceFromContainer {

  private final PostgreSQLContainer<?> container;

  public DataSource get() {
    if (!container.isRunning()) {
      container.start();
    }
    return DataSourceBuilder.create()
            .driverClassName(container.getDriverClassName())
            .url(container.getJdbcUrl())
            .username(container.getUsername())
            .password(container.getPassword())
            .build();
  }
}
