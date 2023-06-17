package io.packstrap.transactionparsercli.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
