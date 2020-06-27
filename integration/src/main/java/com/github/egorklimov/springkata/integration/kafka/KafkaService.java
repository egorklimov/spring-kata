package com.github.egorklimov.springkata.integration.kafka;


import com.github.egorklimov.springkata.integration.ThrowingFunction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@RequiredArgsConstructor
public class KafkaService<K, V> {

  private final String bootstrapServers;
  private final String groupId;
  private final Deserializer<K> keyDeserializer;
  private final Deserializer<V> valueDeserializer;

  @SneakyThrows
  public <T> T queryFor(String topic, final ThrowingFunction<Stream<Map.Entry<K, V>>, T> streamProcessor) {
    try (Consumer<K, V> consumer = createConsumer(topic)) {
      return streamProcessor.apply(
              StreamSupport.stream(
                      consumer.poll(Duration.ofSeconds(10))
                              .records(topic)
                              .spliterator(),
                      false
              ).map(record -> new AbstractMap.SimpleImmutableEntry<>(record.key(), record.value()))
      );
    } catch (Exception e) {
      throw new KafkaServiceException(e);
    }
  }

  private Consumer<K, V> createConsumer(String topicName) {
    final Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    KafkaConsumer<K, V> consumer = new KafkaConsumer<>(
            props,
            keyDeserializer,
            valueDeserializer
    );
    consumer.subscribe(Collections.singletonList(topicName));
    return consumer;
  }
}
