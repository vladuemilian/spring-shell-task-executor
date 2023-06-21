package io.packstrap.taskexecutor.module.transaction.task;

import io.packstrap.taskexecutor.module.transaction.dto.TransactionDto;
import io.packstrap.taskexecutor.module.transaction.service.TransactionContextService;
import io.packstrap.taskexecutor.module.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.shell.command.CommandContext;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JsonWriterTest {

    private final TransactionContextService transactionContextService = Mockito.mock(TransactionContextService.class);
    private final TransactionService transactionService = Mockito.mock(TransactionService.class);
    private final JsonWriter jsonWriter = new JsonWriter(transactionService, transactionContextService);

    @Test
    public void should_print() {
        //GIVEN
        TransactionDto transactionDto = new TransactionDto();

        CommandContext commandContext = Mockito.mock(CommandContext.class);
        when(transactionContextService.getTransactionDto(commandContext)).thenReturn(transactionDto);

        //WHEN
        jsonWriter.execute(commandContext);

        //THEN
        verify(transactionService).jsonPrint(transactionDto);
    }
}