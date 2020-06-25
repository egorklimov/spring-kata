package com.github.egorklimov.springkata.integration;

import java.util.function.Function;

@FunctionalInterface
public interface ExceptionSafeFunction <F, T> {

  T apply(F from) throws Exception;

  static <F, T> Function<F, T> suppressed(ExceptionSafeFunction<F, T> throwing) {
    return from -> {
      try {
        return throwing.apply(from);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    };
  }
}
