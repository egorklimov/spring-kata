package com.github.egorklimov.springkata.api.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticInfo {
  private double min;
  private double max;
  private double sum;

  public AnalyticInfo(ExtendedAnalyticInfo extendedAnalyticInfo) {
    this(
            extendedAnalyticInfo.getMin(),
            extendedAnalyticInfo.getMax(),
            extendedAnalyticInfo.getSum()
    );
  }
}
