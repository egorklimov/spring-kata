package com.github.egorklimov.springkata.api.category;

import com.github.egorklimov.springkata.api.category.model.AnalyticInfo;
import com.github.egorklimov.springkata.api.category.model.AnalyticInfoByCategoryFromTransactionList;
import com.github.egorklimov.springkata.api.category.model.ExtendedAnalyticInfo;
import com.github.egorklimov.springkata.api.category.model.ExtendedAnalyticInfoByCategoryFromTransactionList;
import com.github.egorklimov.springkata.api.category.model.ExtendedUserTransactionInfo;
import com.github.egorklimov.springkata.api.category.model.TotalSumFromAnalyticInfo;
import com.github.egorklimov.springkata.api.category.model.TotalSumFromExtendedAnalyticInfo;
import com.github.egorklimov.springkata.api.category.model.TransactionInfo;
import com.github.egorklimov.springkata.api.category.model.TransactionInfoFromKafka;
import com.github.egorklimov.springkata.api.category.model.UserTransactionInfo;
import com.github.egorklimov.springkata.integration.kafka.KafkaService;
import com.google.common.base.Suppliers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class CategoryAnalysisService {

  private final KafkaService<String, String> kafkaService;

  @Autowired
  public CategoryAnalysisService(KafkaService<String, String> kafkaService) {
    this.kafkaService = kafkaService;
  }

  public Supplier<Map<String, UserTransactionInfo>> computedStats() {
    return Suppliers.memoize(this::transactionsByUser);
  }

  public Supplier<Map<String, ExtendedUserTransactionInfo>> computedExtendedStats() {
    return Suppliers.memoize(this::extendedTransactionsByUser);
  }

  private Map<String, UserTransactionInfo> transactionsByUser() {
    return new TransactionInfoFromKafka(kafkaService)
            .filteredRecords().stream()
            .collect(Collectors.groupingBy(TransactionInfo::getUserId))
            .entrySet().stream()
            .map(userAndTransactions -> {
              Map<String, AnalyticInfo> stats = new AnalyticInfoByCategoryFromTransactionList(userAndTransactions.getValue()).compute();
              return new UserTransactionInfo(
                      userAndTransactions.getKey(),
                      new TotalSumFromAnalyticInfo(stats).computeSum(),
                      stats
              );
            }).collect(
                    Collectors.toMap(
                            UserTransactionInfo::getUserId,
                            Function.identity())
            );
  }

  private Map<String, ExtendedUserTransactionInfo> extendedTransactionsByUser() {
    return new TransactionInfoFromKafka(kafkaService)
            .filteredRecords().stream()
            .collect(Collectors.groupingBy(TransactionInfo::getUserId))
            .entrySet().stream()
            .map(userAndTransactions -> {
              Map<String, ExtendedAnalyticInfo> stats = new ExtendedAnalyticInfoByCategoryFromTransactionList(userAndTransactions.getValue()).compute();
              return new ExtendedUserTransactionInfo(
                      userAndTransactions.getKey(),
                      new TotalSumFromExtendedAnalyticInfo(stats).computeSum(),
                      stats
              );
            }).collect(
                    Collectors.toMap(
                            ExtendedUserTransactionInfo::getUserId,
                            Function.identity())
            );
  }
}
