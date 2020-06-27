package com.github.egorklimov.springkata.api.category.model;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class TotalSumFromAnalyticInfo {

  private final Map<String, AnalyticInfo> analyticInfoMap;

  public double computeSum() {
    return analyticInfoMap.values()
            .stream()
            .map(AnalyticInfo::getSum)
            .mapToDouble(Double::doubleValue)
            .sum();
  }
}
