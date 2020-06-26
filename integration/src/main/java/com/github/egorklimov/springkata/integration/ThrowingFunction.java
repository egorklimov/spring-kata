package com.github.egorklimov.springkata.integration;

@FunctionalInterface
public interface ThrowingFunction<F, T> {
  T apply(F from) throws Exception;
}
