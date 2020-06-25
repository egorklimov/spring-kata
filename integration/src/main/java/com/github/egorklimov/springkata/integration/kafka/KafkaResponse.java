package com.github.egorklimov.springkata.integration.kafka;

import com.github.egorklimov.springkata.integration.RemoteResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class KafkaResponse implements RemoteResponse {
  private final List<String> recors;
}
