package com.github.egorklimov.springkata.integration.kafka;


import com.github.egorklimov.springkata.integration.ExceptionSafeFunction;
import com.github.egorklimov.springkata.integration.RemoteDataSupplier;


public class KafkaSource implements RemoteDataSupplier<KafkaEntityIdentifier, KafkaResponse> {

  @Override
  public <T> T get(final KafkaEntityIdentifier targetIdentifier, final ExceptionSafeFunction<KafkaResponse, T> mapper) {
    return null;
  }
}
