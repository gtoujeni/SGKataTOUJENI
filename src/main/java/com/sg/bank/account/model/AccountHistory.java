package com.sg.bank.account.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountHistory implements Serializable {
    private Transaction transaction;
    private Double balance;
}
