package com.github.egorklimov.springkata.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  @GetMapping("admin/health")
  public HealthCheckResponse incrementAndGet() {
    return new HealthCheckResponse("UP");
  }

  @Data
  @RequiredArgsConstructor
  private static class HealthCheckResponse {
    private final String status;
  }
}
