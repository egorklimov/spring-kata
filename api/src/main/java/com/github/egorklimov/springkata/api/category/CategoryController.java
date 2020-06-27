package com.github.egorklimov.springkata.api.category;

import com.github.egorklimov.springkata.api.category.model.UserTransactionInfo;
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

  @Autowired
  public CategoryController(CategoryAnalysisService service) {
    this.service = service;
    transactionInfoMap = service.computedStats().get();
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

  private static class UserNotFoundResponse {
    private final String status;

    private UserNotFoundResponse() {
      status = "user not found";
    }

    @Override
    public String toString() {
      return String.format("{\"status\":”%s\"}", status);
    }
  }
}
