package com.github.egorklimov.springkata.integration.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
class KafkaServiceTest {

  private static final String INT_TOPIC = "int";
  private static final String STRING_TOPIC = "str";
  private static final KafkaContainer kafkaContainer = new KafkaContainer();

  @BeforeAll
  static void startContainer() {
    kafkaContainer.start();
    try (Producer<String, String> producer = createStringProducer()) {
      IntStream.range(0, 10)
              .peek(i -> log.info("Sending {} to string stream", i))
              .mapToObj(String::valueOf)
              .forEach(i -> producer.send(new ProducerRecord<>(STRING_TOPIC, i, i)));
    } catch (Exception ignored) {
      //skip
    }

    try (Producer<Integer, Integer> producer = createIntProducer()) {
      IntStream.range(0, 10)
              .peek(i -> log.info("Sending {} to int stream", i))
              .forEach(i -> producer.send(new ProducerRecord<>(INT_TOPIC, i, i)));
    } catch (Exception ignored) {
      //skip
    }
  }

  @Test
  void readIntsFromKafkaTest() {
    List<Integer> ints = new KafkaService<Integer, Integer>(
            kafkaContainer.getBootstrapServers(),
            "KafkaExampleProducer",
            new IntegerDeserializer(),
            new IntegerDeserializer()
    ).queryFor(
            INT_TOPIC,
            records -> records.map(Map.Entry::getValue)
                    .collect(Collectors.toList())
    );
    Assertions.assertEquals(
            IntStream.range(0, 10)
                    .boxed()
                    .collect(Collectors.toList()),
            ints
    );
  }

  @Test
  void readStringsFromKafkaTest() {
    List<Integer> ints = new KafkaService<String, String>(
            kafkaContainer.getBootstrapServers(),
            "KafkaExampleProducer",
            new StringDeserializer(),
            new StringDeserializer()
    ).queryFor(
            STRING_TOPIC,
            records -> records.map(Map.Entry::getValue)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList())
    );
    Assertions.assertEquals(
            IntStream.range(0, 10)
                    .boxed()
                    .collect(Collectors.toList()),
            ints
    );
  }

  private static Producer<String, String> createStringProducer() {
    return new KafkaProducer<>(
            ImmutableMap.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers(),
                    ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString()
            ),
            new StringSerializer(),
            new StringSerializer()
    );
  }

  private static Producer<Integer, Integer> createIntProducer() {
    return new KafkaProducer<>(
            ImmutableMap.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers(),
                    ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString()
            ),
            new IntegerSerializer(),
            new IntegerSerializer()
    );
  }
}
