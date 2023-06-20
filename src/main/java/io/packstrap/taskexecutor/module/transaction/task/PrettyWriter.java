package io.packstrap.taskexecutor.module.transaction.task;

import io.packstrap.taskexecutor.Discoverable;
import io.packstrap.taskexecutor.Task;
import io.packstrap.taskexecutor.module.transaction.dto.TransactionDto;
import io.packstrap.taskexecutor.module.transaction.service.TransactionContextService;
import io.packstrap.taskexecutor.module.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrettyWriter implements Task, Discoverable {

    private final TransactionService transactionService;
    private final TransactionContextService transactionContextService;
    @Override
    public CommandRegistration command() {
        return CommandRegistration.builder()
                .command("pretty-writer")
                .description("Reads the transaction from the context of from a file and prints it.")
                .group(TaskType.WRITER.name())
                .withTarget()
                .consumer(this::execute)
                .and()
                .withOption()
                    .required(false)
                    .longNames("file")
                    .shortNames('f')
                    .description("The JSON file that needs to be pretty printed.")
                .and()
                .build();
    }

    @Override
    public Object execute(CommandContext input) {
        TransactionDto transactionDto = transactionContextService.getTransactionDto(input);
        String prettyString = transactionService.prettyPrint(transactionDto);
        System.out.println(prettyString);
        return prettyString;
    }
}
