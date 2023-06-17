package io.packstrap.transactionparsercli.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class TransactionDto {
    private String inputFilename;
    private Long processedRecords;
    private BigDecimal totalTransactionAmount;
    private Map<LocalDate, BigDecimal> amountsPerDay;
}
