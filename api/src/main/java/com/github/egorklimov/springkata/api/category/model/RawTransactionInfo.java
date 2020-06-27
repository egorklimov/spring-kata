package com.github.egorklimov.springkata.api.category.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class RawTransactionInfo {
  private static final ObjectMapper mapper = new ObjectMapper();
  static {
    mapper.registerModule(new JavaTimeModule());
  }

  private final String rawRecord;

  @SneakyThrows
  public TransactionInfo get() {
    return mapper.readValue(rawRecord, TransactionInfo.class);
  }

  public boolean isValid() {
    try {
      mapper.readValue(rawRecord, TransactionInfo.class);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
