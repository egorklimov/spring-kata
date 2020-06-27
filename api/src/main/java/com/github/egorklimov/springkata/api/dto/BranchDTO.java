package com.github.egorklimov.springkata.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
  private Long id;
  private String title;
  private Double lon;
  private Double lat;
  private String address;
}
