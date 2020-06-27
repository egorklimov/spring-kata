package com.github.egorklimov.springkata.api.category.model;

import com.github.egorklimov.springkata.integration.kafka.KafkaService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TransactionInfoFromKafka {
  private static final String PAYMENTS_TOPIC = "RAW_PAYMENTS";

  private final KafkaService<String, String> kafkaService;

  public List<TransactionInfo> filteredRecords() {
    return kafkaService.queryFor(
            PAYMENTS_TOPIC,
            records -> records.map(Map.Entry::getValue)
                    .map(RawTransactionInfo::new)
                    .filter(RawTransactionInfo::isValid)
                    .map(RawTransactionInfo::get)
                    .collect(Collectors.toList())
    );
  }
}
