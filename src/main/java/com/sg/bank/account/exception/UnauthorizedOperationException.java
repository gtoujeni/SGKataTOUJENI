package com.sg.bank.account.exception;

import com.sg.bank.account.model.Account;
import com.sg.bank.account.model.TransactionType;

public class UnauthorizedOperationException extends Exception {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Unauthorized Transaction %s on this account %s";

    public UnauthorizedOperationException(Account account, TransactionType transactionType) {
        super(String.format(MESSAGE, transactionType, account.getAccountId()));
    }
}
