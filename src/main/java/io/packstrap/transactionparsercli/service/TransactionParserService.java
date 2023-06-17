package io.packstrap.transactionparsercli.service;

import io.packstrap.transactionparsercli.Discoverable;
import io.packstrap.transactionparsercli.ServiceException;
import io.packstrap.transactionparsercli.dto.TransactionDto;
import io.packstrap.transactionparsercli.reader.TransactionReader;
import io.packstrap.transactionparsercli.writer.TransactionWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionParserService {
    private final ApplicationContext applicationContext;

    public TransactionDto read(Object input, String readerName) {
        TransactionReader reader = getBean(TransactionReader.class, readerName);
        return reader.read(input);
    }

    public boolean write(TransactionDto transactionDto, String writerName) {
        TransactionWriter writer = getBean(TransactionWriter.class, writerName);
        writer.write(transactionDto);
        return true;
    }

    private <T extends Discoverable> T getBean(Class<T> clazz, String discoverableName) {
        List<T> readers = applicationContext.getBeansOfType(clazz)
                .values()
                .stream()
                .filter(transactionReader -> StringUtils.equalsIgnoreCase(transactionReader.name(), discoverableName))
                .toList();

        if (readers.size() != 1) {
            log.error("Invalid input for: {}", discoverableName);
            throw new ServiceException("Invalid input for: " + discoverableName);
        }
        return readers.get(0);
    }
}
