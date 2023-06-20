package io.packstrap.taskexecutor.module.transaction.service;

import io.packstrap.taskexecutor.ServiceException;
import io.packstrap.taskexecutor.module.transaction.TransactionContext;
import io.packstrap.taskexecutor.module.transaction.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.CommandContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionContextService {
    private final TransactionContext transactionContext;
    private final TransactionService transactionService;

    public TransactionDto getTransactionDto(CommandContext input) {
        Optional<String> file = Optional.ofNullable(input.getOptionValue("file"));
        Optional<TransactionDto> transactionDtoOptional = Optional.empty();

        if(transactionContext.exists()) {
            transactionDtoOptional = Optional.of(transactionContext.getLastTransactionDto());
        }

        if(file.isPresent()) {
            transactionDtoOptional = Optional.of(transactionService.read(file.get()));
        }

        if(transactionDtoOptional.isEmpty()) {
            throw new ServiceException("No transactionDto found. Please read a transaction using any given reader" +
                    " or provide a file as argument.");
        }

        return transactionDtoOptional.get();
    }
}
