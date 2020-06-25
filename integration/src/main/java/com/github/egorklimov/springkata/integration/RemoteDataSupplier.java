package com.github.egorklimov.springkata.integration;

@FunctionalInterface
public interface RemoteDataSupplier <I extends Identifier, R extends RemoteResponse> {
  <T> T get(I targetIdentifier, ExceptionSafeFunction<R, T> mapper);
}
