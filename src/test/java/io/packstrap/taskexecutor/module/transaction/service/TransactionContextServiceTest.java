package io.packstrap.taskexecutor.module.transaction.service;

import io.packstrap.taskexecutor.module.transaction.TransactionContext;
import io.packstrap.taskexecutor.module.transaction.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.shell.command.CommandContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

public class TransactionContextServiceTest {
    private final TransactionService transactionService = Mockito.mock(TransactionService.class);
    private final TransactionContext transactionContext = Mockito.mock(TransactionContext.class);
    private final TransactionContextService transactionContextService = new TransactionContextService(transactionContext, transactionService);
    @Test
    public void should_get_transaction_from_context() {
        //GIVEN
        CommandContext commandContext = Mockito.mock(CommandContext.class);

        when(transactionContext.exists()).thenReturn(true);
        when(transactionContext.getLastTransactionDto()).thenReturn(new TransactionDto("test-filename"));

        //WHEN
        TransactionDto transactionDto = transactionContextService.getTransactionDto(commandContext);

        //THEN
        assertNotNull(transactionDto);
        assertThat(transactionDto.getInputFilename()).isEqualTo("test-filename");
    }

    @Test
    public void should_get_transaction_from_file() {
        //GIVEN
        CommandContext commandContext = Mockito.mock(CommandContext.class);

        when(transactionContext.exists()).thenReturn(false);
        when(commandContext.getOptionValue("file")).thenReturn("test-filename");
        when(transactionService.read("test-filename")).thenReturn(new TransactionDto("test-filename"));

        //WHEN
        TransactionDto transactionDto = transactionContextService.getTransactionDto(commandContext);

        //THEN
        assertNotNull(transactionDto);
        assertThat(transactionDto.getInputFilename()).isEqualTo("test-filename");
    }

}