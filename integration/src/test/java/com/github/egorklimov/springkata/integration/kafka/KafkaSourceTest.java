package com.github.egorklimov.springkata.integration.kafka;

import com.github.egorklimov.springkata.integration.ExceptionSafeFunction;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
class KafkaSourceTest {

  private static final KafkaContainer kafkaContainer = new KafkaContainer();

  @BeforeAll
  static void startContainer() {
    kafkaContainer.start();
    final Producer<String, String> producer = createProducer();
    try {
      IntStream.range(0, 10)
              .peek(i -> log.info("Sending {} to kafka", i))
              .mapToObj(String::valueOf)
              .forEach(i -> producer.send(new ProducerRecord<>("int", i, i)));
    } finally {
      producer.flush();
      producer.close();
    }
  }

  @Test
  void readIntFromKafkaTest() {
    IntsFromKafka ints = new KafkaSource(
            kafkaContainer.getBootstrapServers(),
            "KafkaExampleProducer"
    ).get(
            IntsFromKafka.kafkaSelector(),
            IntsFromKafka.kafkaMapper
    );
    Assertions.assertEquals(
            IntStream.range(0, 10)
                    .boxed()
                    .collect(Collectors.toList()),
            ints.getValue());
  }

  private static Producer<String, String> createProducer() {
    return new KafkaProducer<>(
            ImmutableMap.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers(),
                    ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString()
            ),
            new StringSerializer(),
            new StringSerializer()
    );
  }

  @Data
  @RequiredArgsConstructor
  private static class IntsFromKafka {
    private final List<Integer> value;

    public static KafkaEntityIdentifier kafkaSelector() {
      return new KafkaEntityIdentifier("int");
    }

    public static ExceptionSafeFunction<KafkaResponse, IntsFromKafka> kafkaMapper =
            response -> new IntsFromKafka(
                    response.getRecors().stream()
                            .map(Integer::valueOf)
                            .collect(Collectors.toList())
            );
  }
}
