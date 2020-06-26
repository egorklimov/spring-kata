package com.github.egorklimov.springkata.integration.jdbc;

public class JdbcServiceException extends RuntimeException {
  public JdbcServiceException(Throwable cause) {
    super(cause);
  }
}
