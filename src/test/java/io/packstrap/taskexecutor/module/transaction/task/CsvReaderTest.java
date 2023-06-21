package io.packstrap.taskexecutor.module.transaction.task;

import io.packstrap.taskexecutor.module.transaction.TransactionContext;
import io.packstrap.taskexecutor.module.transaction.dto.TransactionDto;
import io.packstrap.taskexecutor.module.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.shell.command.CommandContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CsvReaderTest {
    private final TransactionService transactionService = Mockito.mock(TransactionService.class);
    private final TransactionContext transactionContext = Mockito.mock(TransactionContext.class);
    private final CsvReader csvReader = new CsvReader(transactionService, transactionContext);

    @Test
    public void should_read_csv_transaction() {
        //GIVEN
        TransactionDto transactionDto = new TransactionDto();

        CommandContext commandContext = Mockito.mock(CommandContext.class);
        when(commandContext.getOptionValue("file")).thenReturn("test-filename");
        when(commandContext.getOptionValue("output")).thenReturn("test-output-filename");

        when(transactionService.csvRead("test-filename")).thenReturn(transactionDto);

        //WHEN
        boolean result = (Boolean) csvReader.execute(commandContext);

        //THEN
        assertThat(result).isTrue();
        verify(transactionService).csvRead("test-filename");
        verify(transactionContext).setLastTransactionDto(transactionDto);
        verify(transactionService).write(transactionDto, Optional.of("test-output-filename"));
    }
}