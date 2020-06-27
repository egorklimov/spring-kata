package com.github.egorklimov.springkata.integration.api;

public class RestApiServiceException extends RuntimeException {
  public RestApiServiceException(Throwable cause) {
    super(cause);
  }
}
