package io.packstrap.taskexecutor.module.transaction.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.packstrap.taskexecutor.ServiceException;
import io.packstrap.taskexecutor.module.transaction.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
    private final ResourceLoader resourceLoader = Mockito.mock(ResourceLoader.class);
    private TransactionService transactionService = new TransactionService(objectMapper, resourceLoader);

    @Test
    public void should_write_transaction() {
        //GIVEN
        Map<LocalDate, BigDecimal> amounts = new HashMap<>();
        amounts.put(LocalDate.now(), BigDecimal.TEN);

        String filename = UUID.randomUUID().toString();
        TransactionDto transactionDto = new TransactionDto("classpath:/" + filename);
        transactionDto.setAmountsPerDay(amounts);

        //WHEN
        boolean result = transactionService.write(transactionDto, Optional.of(filename));

        //THEN
        assertThat(result).isTrue();

        File file = new File(filename);
        assertThat(file.exists()).isTrue();
        file.delete();
    }

    @Test
    public void should_read_transaction() throws Exception {
        //GIVEN
        String filename = UUID.randomUUID().toString();
        TransactionDto transactionDto = new TransactionDto("file://" + filename);
        transactionDto.setProcessedRecords(12L);
        Map<LocalDate, BigDecimal> amounts = new HashMap<>();
        amounts.put(LocalDate.now(), BigDecimal.TEN);
        transactionDto.setAmountsPerDay(amounts);

        Resource resource = Mockito.mock(Resource.class);
        InputStream dtoStream = new ByteArrayInputStream(objectMapper.writeValueAsBytes(transactionDto));

        when(resource.getInputStream()).thenReturn(dtoStream);
        when(resourceLoader.getResource("file://" + filename)).thenReturn(resource);

        //WHEN
        TransactionDto readDto = transactionService.read(filename);

        //WHEN
        assertThat(readDto).isNotNull();
        assertThat(readDto.getProcessedRecords()).isEqualTo(transactionDto.getProcessedRecords());
    }

    @Test
    public void should_read_csv_transaction() throws Exception {
        //GIVEN
        String filename = "test.csv";

        String csvFileContent = "address,age,email,firstName,ip,joinDate,lastName,leaveDate,referral,transactionAmount,transactionDate,zip\n" +
                "test,10,foo@foo,Denis,0.0.0.0,2019-01-01,Degterev,2019-01-01,foo,10.0,Mon Apr 29 2019 07:47:09 GMT-0600 (Mountain Daylight Time),12345\n" +
                "pck,10,pck@foo,Kevin,0.0.0.0,2019-01-01,Boston,2019-01-01,foo,15.3,Mon Apr 29 2019 07:47:09 GMT-0600 (Mountain Daylight Time),12345\n" +
                "foo,12,bar@bar,Ritchie,127.0.0.1,2019-01-01,Blackmore,2019-01-01,bar,12.0,Mon Apr 29 2019 07:47:09 GMT-0600 (Mountain Daylight Time),54321\n";

        given_resource(filename, csvFileContent);

        //WHEN
        TransactionDto transactionDto = transactionService.csvRead(filename);

        //THEN
        assertThat(transactionDto).isNotNull();
        assertThat(transactionDto.getProcessedRecords()).isEqualTo(3L);
        assertThat(transactionDto.getAmountsPerDay()).isNotNull();
        assertThat(transactionDto.getAmountsPerDay().size()).isEqualTo(1);
        assertThat(transactionDto.getAmountsPerDay().get(LocalDate.of(2019, 4, 29))).isEqualTo(BigDecimal.valueOf(37.3));
    }
    @Test
    public void should_fail_for_invalid_transaction_amount() throws Exception {
        //GIVEN
        String filename = "test.csv";

        String csvFileContent = "address,age,email,firstName,ip,joinDate,lastName,leaveDate,referral,transactionAmount,transactionDate,zip\n" +
                "test,10,foo@foo,Denis,0.0.0.0,2019-01-01,Degterev,2019-01-01,foo,10.0,INVALID_DATE,12345\n";

        given_resource(filename, csvFileContent);

        //WHEN
        ServiceException thrown = assertThrows(ServiceException.class, () -> transactionService.csvRead(filename));
        assertThat(thrown.getMessage()).startsWith("Invalid date format on line: 1");
    }

    @Test
    public void should_fail_for_invalid_transaction_date() throws Exception {
        //GIVEN
        String filename = "test.csv";

        String csvFileContent = "address,age,email,firstName,ip,joinDate,lastName,leaveDate,referral,transactionAmount,transactionDate,zip\n" +
                "test,10,foo@foo,Denis,0.0.0.0,2019-01-01,Degterev,2019-01-01,foo,INVALID,Mon Apr 29 2019 07:47:09 GMT-0600 (Mountain Daylight Time),12345\n";

        given_resource(filename, csvFileContent);

        //WHEN
        ServiceException thrown = assertThrows(ServiceException.class, () -> transactionService.csvRead(filename));
        assertThat(thrown.getMessage()).startsWith("Invalid transaction amount on line: 1");
    }

    @Test
    public void should_pretty_print() {
        //GIVEN
        Map<LocalDate, BigDecimal> amounts = new HashMap<>();
        amounts.put(LocalDate.of(2023, 6, 20), BigDecimal.TEN);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTotalTransactionAmount(BigDecimal.TEN);
        transactionDto.setProcessedRecords(12L);
        transactionDto.setInputFilename("test.csv");
        transactionDto.setAmountsPerDay(amounts);

        //WHEN
        String pretty = transactionService.prettyPrint(transactionDto);

        //THEN
        assertThat(pretty).isEqualTo("==== Run Summary ====\n" +
                "Input filename: src/main/test/resources/transaction_data.csv\n" +
                "Total records processed: 12\n" +
                "Total transaction amount: 10\n" +
                "Transaction amount per day:\n" +
                "2023-06-20: 10\n" +
                "==== End Run Summary ====");
    }

    @Test
    public void should_json_print() {
        //GIVEN
        Map<LocalDate, BigDecimal> amounts = new HashMap<>();
        amounts.put(LocalDate.of(2023, 6, 20), BigDecimal.TEN);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTotalTransactionAmount(BigDecimal.TEN);
        transactionDto.setProcessedRecords(12L);
        transactionDto.setInputFilename("test.csv");
        transactionDto.setAmountsPerDay(amounts);

        //WHEN
        String pretty = transactionService.jsonPrint(transactionDto);

        //THEN
        assertThat(pretty).isEqualTo("{\"Input filename\":\"test.csv\",\"Total records processed\":12,\"Total transaction amount\":10,\"Daily Transaction Amounts\":{\"2023-06-20\":10}}");
    }

    private Resource given_resource(String filename, String content) throws Exception {
        Resource resource = Mockito.mock(Resource.class);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        when(resource.exists()).thenReturn(true);
        when(resourceLoader.getResource("file://" + filename)).thenReturn(resource);
        return resource;
    }
}