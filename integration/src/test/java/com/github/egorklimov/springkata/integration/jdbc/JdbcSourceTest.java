package com.github.egorklimov.springkata.integration.jdbc;

import com.github.egorklimov.springkata.integration.ExceptionSafeFunction;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.ResultSet;

class JdbcSourceTest {

  private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>();

  @Test
  void checkSimpleQuery() {
    JdbcSource source = new JdbcSource(new DataSourceFromContainer(postgreSQLContainer).get());
    IntDatabaseRecord record = source.get(
            IntDatabaseRecord.jdbcSelector(),
            IntDatabaseRecord.jdbcMapper
    );
    Assertions.assertEquals(1, record.getValue());
  }

  @Data
  @RequiredArgsConstructor
  private static class IntDatabaseRecord {
    private final Integer value;

    public static JdbcEntityIdentifier jdbcSelector() {
      return new JdbcEntityIdentifier("SELECT 1 AS VALUE;");
    }

    public static ExceptionSafeFunction<JdbcResponse, IntDatabaseRecord> jdbcMapper = response -> {
      final ResultSet rs = response.getResultSet();
      while (rs.next()) {
        return new IntDatabaseRecord(rs.getInt("VALUE"));
      }
      throw new RuntimeException("Aaa");
    };
  }

}