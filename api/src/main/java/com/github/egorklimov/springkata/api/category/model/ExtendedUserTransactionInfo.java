package com.github.egorklimov.springkata.api.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedUserTransactionInfo {
  private String userId;
  private Double totalSum;
  private Map<String, ExtendedAnalyticInfo> analyticInfo;
}
