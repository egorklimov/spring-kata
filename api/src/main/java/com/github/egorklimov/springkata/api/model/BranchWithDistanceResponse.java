package com.github.egorklimov.springkata.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchWithDistanceResponse {
  private Long id;
  private String title;
  private Double lon;
  private Double lat;
  private String address;
  private Long distance;
}
