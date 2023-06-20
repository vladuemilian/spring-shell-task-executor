package io.packstrap.taskexecutor.module.transaction.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Transaction {
    private String address;
    private Integer age;
    private String email;
    private String firstName;
    private String lastName;
    private String ip;
    private ZonedDateTime joinDate;
    private ZonedDateTime leaveDate;
    private Boolean referral;
    private BigDecimal transactionAmount;
    private ZonedDateTime transactionDate;
    private String zip;
}
