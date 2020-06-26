package com.github.egorklimov.springkata.integration.kafka;


import com.github.egorklimov.springkata.integration.ExceptionSafeFunction;
import com.github.egorklimov.springkata.integration.RemoteDataSupplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RequiredArgsConstructor
public class KafkaSource implements RemoteDataSupplier<KafkaEntityIdentifier, KafkaResponse> {

  private final String bootstrapServers;
  private final String groupId;

  @SneakyThrows
  @Override
  public <T> T get(final KafkaEntityIdentifier targetIdentifier, final ExceptionSafeFunction<KafkaResponse, T> mapper) {
    Consumer<String, String> consumer = createConsumer(targetIdentifier);
    return mapper.apply(
            new KafkaResponse(
                    StreamSupport.stream(
                            consumer.poll(Duration.ofSeconds(10))
                                    .records(targetIdentifier.getTopic())
                                    .spliterator(),
                            false
                    ).map(ConsumerRecord::value)
                            .collect(Collectors.toList())
            )
    );
  }

  private Consumer<String, String> createConsumer(KafkaEntityIdentifier kafkaEntityIdentifier) {
    final Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
            props,
            new StringDeserializer(),
            new StringDeserializer()
    );
    consumer.subscribe(Collections.singletonList(kafkaEntityIdentifier.getTopic()));
    return consumer;
  }
}
