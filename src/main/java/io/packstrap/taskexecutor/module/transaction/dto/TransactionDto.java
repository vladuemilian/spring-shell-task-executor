package io.packstrap.taskexecutor.module.transaction.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.packstrap.taskexecutor.configuration.ObjectMapperConfiguration;
import io.packstrap.taskexecutor.module.transaction.domain.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
//technically speaking, not the dto but the domain object since it is the principal object of the transaction module.
public class TransactionDto {
    public TransactionDto(String inputFilename) {
        this.inputFilename = inputFilename;

        if(processedRecords == null) {
            processedRecords = 0L;
        }
        if(totalTransactionAmount == null) {
            totalTransactionAmount = BigDecimal.ZERO;
        }
        if(amountsPerDay == null) {
            amountsPerDay = new HashMap<>();
        }
    }

    private String inputFilename;
    private Long processedRecords;
    private BigDecimal totalTransactionAmount;
    @JsonDeserialize(using = ObjectMapperConfiguration.LocalDateDeserializer.class)
    private Map<LocalDate, BigDecimal> amountsPerDay;

    public void process(Transaction transaction) {
        this.processedRecords++;
        this.totalTransactionAmount = this.totalTransactionAmount.add(transaction.getTransactionAmount());

        LocalDate transactionDate = transaction.getTransactionDate().toLocalDate();
        if(this.amountsPerDay.containsKey(transactionDate)) {
            this.amountsPerDay.put(transactionDate, this.amountsPerDay.get(transactionDate).add(transaction.getTransactionAmount()));
        } else {
            this.amountsPerDay.put(transactionDate, transaction.getTransactionAmount());
        }
    }
}
