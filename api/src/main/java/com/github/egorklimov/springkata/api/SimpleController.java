package com.github.egorklimov.springkata.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SimpleController {

  private final AtomicLong counter = new AtomicLong(0);

  @GetMapping("api/inc")
  public String incrementAndGet() {
    return String.valueOf(counter.incrementAndGet());
  }
}
