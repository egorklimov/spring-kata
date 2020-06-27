package com.github.egorklimov.springkata.api.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTransactionInfo {
  private String userId;
  private Double totalSum;
  private Map<String, AnalyticInfo> analyticInfo;
}
