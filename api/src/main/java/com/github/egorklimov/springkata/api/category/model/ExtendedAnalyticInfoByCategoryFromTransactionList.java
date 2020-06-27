package com.github.egorklimov.springkata.api.category.model;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ExtendedAnalyticInfoByCategoryFromTransactionList {

  private final List<TransactionInfo> transactions;

  public Map<String, ExtendedAnalyticInfo> compute() {
    return transactions.stream()
            // get summarizingDouble stat for each category
            .collect(
                    Collectors.groupingBy(
                            TransactionInfo::getCategoryId,
                            Collectors.mapping(
                                    TransactionInfo::getAmount,
                                    Collectors.summarizingDouble(Double::doubleValue)
                            )
                    )
            )
            .entrySet().stream()
            // convert summarizingDouble stat to AnalyticInfo
            .collect(
                    Collectors.toMap(
                            categoryAndStatistic -> categoryAndStatistic.getKey().toString(),
                            categoryAndStatistic -> new ExtendedAnalyticInfo(
                                    categoryAndStatistic.getValue().getMin(),
                                    categoryAndStatistic.getValue().getMax(),
                                    categoryAndStatistic.getValue().getSum(),
                                    categoryAndStatistic.getValue().getCount()
                            )
                    )
            );
  }
}




