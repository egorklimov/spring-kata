package com.github.egorklimov.springkata.integration.kafka;

public class KafkaServiceException extends RuntimeException {
  public KafkaServiceException(Throwable cause) {
    super(cause);
  }
}
