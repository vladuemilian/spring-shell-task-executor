package io.packstrap.taskexecutor.module.transaction.dto;

import io.packstrap.taskexecutor.module.transaction.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.*;

public class TransactionDtoTest {
    @Test
    public void should_process() {
        //GIVEN
        String inputFilename = "inputFilename";
        ZonedDateTime transactionDate = ZonedDateTime.now();

        TransactionDto transactionDto = new TransactionDto(inputFilename);

        Transaction transaction = new Transaction();
        transaction.setTransactionDate(transactionDate);
        transaction.setTransactionAmount(BigDecimal.TEN);

        assertThat(transactionDto.getInputFilename()).isEqualTo(inputFilename);
        assertThat(transactionDto.getProcessedRecords()).isEqualTo(0L);
        assertThat(transactionDto.getTotalTransactionAmount()).isEqualTo(BigDecimal.ZERO);

        //WHEN
        transactionDto.process(transaction);

        //THEN
        assertThat(transactionDto.getProcessedRecords()).isEqualTo(1L);
        assertThat(transactionDto.getTotalTransactionAmount()).isEqualTo(BigDecimal.TEN);
    }
}