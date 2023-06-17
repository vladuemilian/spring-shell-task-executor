package io.packstrap.transactionparsercli.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.packstrap.transactionparsercli.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CsvReader implements TransactionReader {
    private ObjectMapper objectMapper;

    @Override
    public String name() {
        return "CSV_READER";
    }

    @Override
    public CommandRegistration command() {
        return CommandRegistration.builder()
                //.withOption().
                .build();
    }

    @Override
    public TransactionDto read(Object input) {
        return null;
    }
}
