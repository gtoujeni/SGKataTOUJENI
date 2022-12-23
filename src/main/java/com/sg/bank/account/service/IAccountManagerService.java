package com.sg.bank.account.service;

import com.sg.bank.account.exception.UnauthorizedOperationException;
import com.sg.bank.account.model.Account;
import com.sg.bank.account.model.Transaction;

public interface IAccountManagerService {
    String showAccountHistory(Account account);

    Transaction depositOperation(Account account, Double amount) throws UnauthorizedOperationException;

    Transaction withdrawalOperation(Account account, Double amount) throws UnauthorizedOperationException;
}
