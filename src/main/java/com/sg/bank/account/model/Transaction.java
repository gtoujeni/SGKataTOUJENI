package com.sg.bank.account.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Builder
public class Transaction implements Serializable {
    private static AtomicLong REFERENCE_TRANSACTION = new AtomicLong(10000);
    private static final long serialVersionID = 1l;

    @Builder.Default
    private Long reference = REFERENCE_TRANSACTION.addAndGet(1);

    private LocalDateTime transactionTimestamp;

    private final Double amount;

    private TransactionType transactionType;

    public Boolean isDepositTransaction() {
        return transactionType.equals(TransactionType.DEPOSIT) ? true : false;
    }

}
