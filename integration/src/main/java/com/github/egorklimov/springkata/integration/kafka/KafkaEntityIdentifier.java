package com.github.egorklimov.springkata.integration.kafka;

import com.github.egorklimov.springkata.integration.Identifier;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class KafkaEntityIdentifier implements Identifier {
  private final String topic;
}
