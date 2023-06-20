package io.packstrap.taskexecutor.module.transaction.task;

import io.packstrap.taskexecutor.module.transaction.service.TransactionContextService;
import io.packstrap.taskexecutor.module.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class PrettyWriterTest {
    private final TransactionService transactionService = Mockito.mock(TransactionService.class);
    private final TransactionContextService transactionContextService = Mockito.mock(TransactionContextService.class);
    private final PrettyWriter prettyWriter = new PrettyWriter(transactionService, transactionContextService);

    @Test
    public void should_pretty_print() {

    }
}