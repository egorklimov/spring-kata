package com.github.egorklimov.springkata.integration.jdbc;

import com.github.egorklimov.springkata.integration.RemoteResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;

@Data
@RequiredArgsConstructor
public class JdbcResponse implements RemoteResponse {
  private final ResultSet resultSet;
}
