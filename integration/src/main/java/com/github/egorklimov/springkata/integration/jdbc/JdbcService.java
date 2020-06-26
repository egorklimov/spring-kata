package com.github.egorklimov.springkata.integration.jdbc;

import com.github.egorklimov.springkata.integration.ThrowingFunction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RequiredArgsConstructor
public class JdbcService {

  private final DataSource dataSource;

  @SneakyThrows
  public <T> T queryFor(String query, final ThrowingFunction<ResultSet, T> mapper) {
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      return mapper.apply(statement.executeQuery(query));
    } catch (Exception e) {
      throw new JdbcServiceException(e);
    }
  }
}
