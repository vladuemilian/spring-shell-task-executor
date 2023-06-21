package io.packstrap.taskexecutor.module.transaction.task;

import io.packstrap.taskexecutor.Discoverable;
import io.packstrap.taskexecutor.Task;
import io.packstrap.taskexecutor.module.transaction.TransactionContext;
import io.packstrap.taskexecutor.module.transaction.dto.TransactionDto;
import io.packstrap.taskexecutor.module.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsvReader implements Task, Discoverable {
    private final TransactionService transactionService;
    private final TransactionContext transactionContext;
    @Override
    public CommandRegistration command() {
        return CommandRegistration.builder()
                .command("csv-reader")
                .description("Reads a CSV file and outputs the content as a Transaction object.")
                .group(TaskType.READER.name())
                .withTarget()
                .consumer(this::execute)
                .and()
                .withOption()
                    .required(true)
                    .longNames("file")
                    .shortNames('f')
                    .description("The output file where the CSV will be transformed to a JSON representation of Transaction object.")
                .and()
                .withOption()
                    .shortNames('o')
                    .longNames("output")
                    .required(false)
                    .description("The output file where the CSV will be transformed to a JSON representation of Transaction object.")
                .and()
                .build();
    }

    @Override
    public Object execute(CommandContext context) {
        String file = context.getOptionValue("file");
        Optional<String> outputFile = Optional.ofNullable(context.getOptionValue("output"));
        TransactionDto transactionDto = transactionService.csvRead(file);
        transactionContext.setLastTransactionDto(transactionDto);
        transactionService.write(transactionDto, outputFile);
        System.out.println("Successfully read CSV file.");
        return true;
    }
}
