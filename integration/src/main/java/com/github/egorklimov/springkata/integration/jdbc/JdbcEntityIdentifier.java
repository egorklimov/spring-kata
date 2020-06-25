package com.github.egorklimov.springkata.integration.jdbc;

import com.github.egorklimov.springkata.integration.Identifier;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JdbcEntityIdentifier implements Identifier {
  private final String query;

  public static JdbcEntityIdentifier of(String query) {
    return new JdbcEntityIdentifier(query);
  }
}
