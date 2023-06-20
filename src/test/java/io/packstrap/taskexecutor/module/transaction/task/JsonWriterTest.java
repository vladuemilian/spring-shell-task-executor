package io.packstrap.taskexecutor.module.transaction.task;

import io.packstrap.taskexecutor.module.transaction.service.TransactionContextService;
import io.packstrap.taskexecutor.module.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    private final TransactionContextService transactionContextService = Mockito.mock(TransactionContextService.class);
    private final TransactionService transactionService = Mockito.mock(TransactionService.class);
    private final JsonWriter jsonWriter = new JsonWriter(transactionService, transactionContextService);

    @Test
    public void should_print() {

    }
}