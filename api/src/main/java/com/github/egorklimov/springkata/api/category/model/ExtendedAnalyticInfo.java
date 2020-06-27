package com.github.egorklimov.springkata.api.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedAnalyticInfo {
  private double min;
  private double max;
  private double sum;
  private long count;
}
