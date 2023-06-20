package io.packstrap.taskexecutor.module.transaction.task;

import io.packstrap.taskexecutor.module.transaction.TransactionContext;
import io.packstrap.taskexecutor.module.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class CsvReaderTest {

    private final TransactionService transactionService = Mockito.mock(TransactionService.class);
    private final TransactionContext transactionContext = Mockito.mock(TransactionContext.class);

    private final CsvReader csvReader = new CsvReader(transactionService, transactionContext);

    @Test
    public void should_read_csv_transaction() {

    }

    @Test
    public void should_pass_output_file() {

    }
}