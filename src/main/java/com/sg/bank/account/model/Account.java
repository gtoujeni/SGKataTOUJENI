package com.sg.bank.account.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Account implements Serializable {
    private static final long SERIAL_VERSION_ID = 1l;

    private Long accountId;
    private Client client;
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();

}
