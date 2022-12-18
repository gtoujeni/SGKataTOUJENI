package com.sg.bank.account.service;


import com.sg.bank.account.model.Account;
import com.sg.bank.account.model.AccountHistory;
import com.sg.bank.account.model.Transaction;

import java.util.List;

public interface IAccountHistoryService {
    List<AccountHistory> accountHistory(Account account);
    Double computeAccountBalance(Transaction transaction, List<Transaction> transactions);
}
