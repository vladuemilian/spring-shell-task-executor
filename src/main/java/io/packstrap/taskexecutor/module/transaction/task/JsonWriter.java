package io.packstrap.taskexecutor.module.transaction.task;

import io.packstrap.taskexecutor.Discoverable;
import io.packstrap.taskexecutor.Task;
import io.packstrap.taskexecutor.module.transaction.dto.TransactionDto;
import io.packstrap.taskexecutor.module.transaction.service.TransactionContextService;
import io.packstrap.taskexecutor.module.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonWriter implements Task, Discoverable {
    private final TransactionService transactionService;
    private final TransactionContextService transactionContextService;
    @Override
    public CommandRegistration command() {
        return CommandRegistration.builder()
                .command("json-writer")
                .description("Reads a transaction from a file or from the context and prints it in a JSON format.")
                .group(TaskType.WRITER.name())
                .withTarget()
                .consumer(this::execute)
                .and()
                .withOption()
                .required(false)
                .longNames("file")
                .shortNames('f')
                .description("The file input to be printed.")
                .and()
                .build();
    }

    @Override
    public Object execute(CommandContext input) {
        TransactionDto transactionDto = transactionContextService.getTransactionDto(input);
        String jsonPrint = transactionService.jsonPrint(transactionDto);
        System.out.println(jsonPrint);
        return jsonPrint;
    }
}
