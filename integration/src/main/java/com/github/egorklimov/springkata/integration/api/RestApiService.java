package com.github.egorklimov.springkata.integration.api;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
public class RestApiService {
  private final RestTemplate template;

  public RestApiService() {
    this.template = new RestTemplate();
  }

  @SneakyThrows
  public <T> Optional<T> queryFor(String url, Class<T> tClass) {
    try {
      return Optional.ofNullable(template.getForObject(url, tClass));
    } catch (RestClientException e) {
      log.info("Ошибка при обращении к {}: {}", url, e.getMessage());
      return Optional.empty();
    }
  }
}
