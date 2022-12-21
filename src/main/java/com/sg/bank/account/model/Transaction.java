package com.sg.bank.account.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Builder
public class Transaction implements Serializable {
    private static AtomicLong referenceTransaction = new AtomicLong(10000);
    private static final long SERIAL_VERSION_ID = 1L;

    @Builder.Default
    private Long reference = referenceTransaction.addAndGet(1);

    private LocalDateTime transactionTimestamp;

    private final Double amount;

    private TransactionType transactionType;

    public Boolean isDepositTransaction() {
        return transactionType.equals(TransactionType.DEPOSIT);
    }

}
