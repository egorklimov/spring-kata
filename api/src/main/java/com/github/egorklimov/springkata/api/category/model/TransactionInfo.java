package com.github.egorklimov.springkata.api.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInfo {
  private String ref;
  private Long categoryId;
  private String userId;
  private String recipientId;
  private String desc;
  private Double amount;
}
