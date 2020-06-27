package com.github.egorklimov.springkata.api.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.egorklimov.springkata.api.category.model.ExtendedUserTransactionInfo;
import com.github.egorklimov.springkata.api.category.model.UserStatisticFromUserTransactionInfo;
import com.github.egorklimov.springkata.api.category.model.UserTransactionInfo;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {

  private final CategoryAnalysisService service;
  private final Map<String, UserTransactionInfo> transactionInfoMap;
  private final Map<String, ExtendedUserTransactionInfo> extendedUserTransactionInfoMap;

  @Autowired
  public CategoryController(CategoryAnalysisService service) {
    this.service = service;
    transactionInfoMap = service.computedStats().get();
    extendedUserTransactionInfoMap = service.computedExtendedStats().get();
  }

  @GetMapping("analytic")
  public List<UserTransactionInfo> analytic() {
    return new ArrayList<>(transactionInfoMap.values());
  }

  @GetMapping("analytic/{userId}")
  public ResponseEntity<?> byUser(@PathVariable("userId") String userId) {
    UserTransactionInfo founded = transactionInfoMap.get(userId);
    if (founded == null) {
      return new ResponseEntity<>(new UserNotFoundResponse().toString(), HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(founded);
  }

  @GetMapping("analytic/{userId}/templates")
  public ResponseEntity<?> templates(@PathVariable("userId") String userId) {
    UserTransactionInfo founded = transactionInfoMap.get(userId);
    if (founded == null) {
      return new ResponseEntity<>(new UserNotFoundResponse().toString(), HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(founded);
  }

  @GetMapping("analytic/{userId}/stats")
  public ResponseEntity<?> stats(@PathVariable("userId") String userId) {
    ExtendedUserTransactionInfo founded = extendedUserTransactionInfoMap.get(userId);
    if (founded == null) {
      return new ResponseEntity<>(new UserNotFoundResponse().toString(), HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(new UserStatisticFromUserTransactionInfo(founded).get());
  }

  @Data
  private static class UserNotFoundResponse {
    private String status;

    private UserNotFoundResponse() {
      status = "user not found";
    }

    @Override
    @SneakyThrows
    public String toString() {
      return new ObjectMapper().writeValueAsString(this);
    }
  }
}
