package com.github.egorklimov.springkata.api.category.model;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class TotalSumFromExtendedAnalyticInfo {

  private final Map<String, ExtendedAnalyticInfo> analyticInfoMap;

  public double computeSum() {
    return analyticInfoMap.values()
            .stream()
            .map(ExtendedAnalyticInfo::getSum)
            .mapToDouble(Double::doubleValue)
            .sum();
  }
}
