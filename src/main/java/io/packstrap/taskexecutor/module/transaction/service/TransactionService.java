package io.packstrap.taskexecutor.module.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.packstrap.taskexecutor.ServiceException;
import io.packstrap.taskexecutor.module.transaction.domain.Transaction;
import io.packstrap.taskexecutor.module.transaction.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d yyyy HH:mm:ss 'GMT'Z (zzzz)");

    public boolean write(TransactionDto transactionDto, Optional<String> filename) {
        if(filename.isEmpty()) {
            throw new ServiceException("No filename provided");
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename.get()));
            writer.write(objectMapper.writeValueAsString(transactionDto));
            writer.close();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return true;
    }

    public TransactionDto read(String inputFilename) {
        Resource resource = resourceLoader.getResource("file://" + inputFilename);
        try {
            String asString = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
            return objectMapper.readValue(asString, TransactionDto.class);
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }
    public TransactionDto csvRead(String inputFilename) {
        Resource resource = resourceLoader.getResource("file://" + inputFilename);

        if(!resource.exists()) {
            throw new ServiceException("File not found: " + inputFilename);
        }
        Pattern pattern = Pattern.compile(",");

        TransactionDto transactionDto = new TransactionDto(inputFilename);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            while(reader.ready()) {
                reader.lines().skip(1).map(line -> {
                    String[] values = pattern.split(line);
                    return Transaction.builder()
                            .transactionAmount(parseTransactionAmount(values, transactionDto.getProcessedRecords()))
                            .transactionDate(parseDate(values, transactionDto.getProcessedRecords()))
                            //NOTE: the other fields are not mapped as they are not used anywhere in the logic
                            .build();
                }).forEach(transactionDto::process);
            }
        } catch (IOException e) {
            throw new ServiceException("Error reading file: " + inputFilename, e);
        }
        return transactionDto;
    }

    private BigDecimal parseTransactionAmount(String[] values, Long line) {
        try {
            return new BigDecimal(values[9]);
        } catch (NumberFormatException e) {
            throw new ServiceException(String.format("Invalid transaction amount on line: %d: %s", line+1, String.join(",", values)));
        }
    }

    private ZonedDateTime parseDate(String[] values, Long line) {
        try {
            return ZonedDateTime.parse(values[10], formatter);
        } catch (DateTimeException e) {
            throw new ServiceException(String.format("Invalid date format on line: %d: %s", line+1, String.join(",", values)));
        }
    }

    public String prettyPrint(TransactionDto transactionDto) {
        String amounts = transactionDto.getAmountsPerDay().entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .reduce((a, b) -> String.format("%s\n%s", a, b))
                .orElse("");
        return String.format("==== Run Summary ====\n" +
                "Input filename: src/main/test/resources/transaction_data.csv\n" +
                "Total records processed: %d\n" +
                "Total transaction amount: %s\n" +
                "Transaction amount per day:\n" +
                "%s\n" +
                "==== End Run Summary ====", transactionDto.getProcessedRecords(),
                transactionDto.getTotalTransactionAmount(),
                amounts);
    }

    public String jsonPrint(TransactionDto transactionDto) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("Input filename", transactionDto.getInputFilename());
        objectNode.put("Total records processed", transactionDto.getProcessedRecords());
        objectNode.put("Total transaction amount", transactionDto.getTotalTransactionAmount());
        objectNode.set("Daily Transaction Amounts", objectMapper.valueToTree(transactionDto.getAmountsPerDay()));
        try {
            return objectMapper.writeValueAsString(objectNode);
        } catch (JsonProcessingException e) {
            throw new ServiceException(e);
        }
    }
}
