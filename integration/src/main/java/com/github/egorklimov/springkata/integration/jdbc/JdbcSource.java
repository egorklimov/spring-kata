package com.github.egorklimov.springkata.integration.jdbc;

import com.github.egorklimov.springkata.integration.ExceptionSafeFunction;
import com.github.egorklimov.springkata.integration.RemoteDataSupplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@RequiredArgsConstructor
public class JdbcSource implements RemoteDataSupplier<JdbcEntityIdentifier, JdbcResponse> {

  private final DataSource dataSource;

  @SneakyThrows
  @Override
  public <T> T get(final JdbcEntityIdentifier targetIdentifier, final ExceptionSafeFunction<JdbcResponse, T> mapper) {
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      return mapper.apply(
              new JdbcResponse(statement.executeQuery(targetIdentifier.getQuery()))
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
