package com.github.egorklimov.springkata.api.category.model;

import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@RequiredArgsConstructor
public class UserStatisticFromUserTransactionInfo {

  private final ExtendedUserTransactionInfo transactionInfo;

  public UserStatistic get() {
    return new UserStatistic(
            Long.parseLong(
                    transactionInfo.getAnalyticInfo().entrySet().stream()
                            .max(Comparator.comparingLong(p -> p.getValue().getCount()))
                            .get().getKey()
            ),
            Long.parseLong(
                    transactionInfo.getAnalyticInfo().entrySet().stream()
                            .min(Comparator.comparingLong(p -> p.getValue().getCount()))
                            .get().getKey()
            ),
            Long.parseLong(
                    transactionInfo.getAnalyticInfo().entrySet().stream()
                            .max(Comparator.comparingDouble(p -> p.getValue().getSum()))
                            .get().getKey()
            ),
            Long.parseLong(
                    transactionInfo.getAnalyticInfo().entrySet().stream()
                            .min(Comparator.comparingDouble(p -> p.getValue().getSum()))
                            .get().getKey()
            )
    );
  }
}
