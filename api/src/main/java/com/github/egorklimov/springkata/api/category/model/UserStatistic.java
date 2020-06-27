package com.github.egorklimov.springkata.api.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatistic {
  private Long oftenCategoryId;
  private Long rareCategoryId;
  private Long maxAmountCategoryId;
  private Long minAmountCategoryId;
}
