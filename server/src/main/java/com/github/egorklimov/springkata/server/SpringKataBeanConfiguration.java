package com.github.egorklimov.springkata.server;

import com.github.egorklimov.springkata.integration.kafka.KafkaService;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ServletComponentScan
@ComponentScan(basePackages = {"com.github.egorklimov.springkata"})
public class SpringKataBeanConfiguration {

  private final String kafkaBootstrapServers;
  private final String kafkaGroupId;

  @Autowired
  public SpringKataBeanConfiguration(@Value("${kafka.bootstrap.servers}") final String kafkaBootstrapServers,
                                     @Value("${kafka.groupId}") final String kafkaGroupId) {
    this.kafkaBootstrapServers = kafkaBootstrapServers;
    this.kafkaGroupId = kafkaGroupId;
  }

  @Bean
  public KafkaService<String, String> kafkaService() {
    return new KafkaService<>(
            this.kafkaBootstrapServers,
            kafkaGroupId,
            new StringDeserializer(),
            new StringDeserializer()
    );
  }
}
